package com.example.moneybook.ui.screen.detail

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneybook.R
import com.example.moneybook.data.Customer
import com.example.moneybook.data.CustomerFinalAmount
import com.example.moneybook.data.CustomerRepository
import com.example.moneybook.data.CustomerTransaction
import com.example.moneybook.ui.screen.home.findBalanceColor
import com.example.moneybook.ui.screen.home.findFormattedAmount
import com.example.moneybook.updateCustomerTransactionInCustomerTable
import com.example.woof.ui.theme.amount_dialog_title_green
import com.example.woof.ui.theme.closing_balance_green
import com.example.woof.ui.theme.closing_balance_red
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val repository: CustomerRepository
) : ViewModel()
{
    val customerId : Int = checkNotNull(savedStateHandle["customer_id"])

//    val customerName : String = checkNotNull(savedStateHandle["customer_name"])

     private var _topBarCustomerDetail = MutableStateFlow(TopBarCustomerDetail())
     val topBarCustomerDetail : StateFlow<TopBarCustomerDetail> = _topBarCustomerDetail.asStateFlow()

    private var _detailScreenState = MutableStateFlow(DetailScreenState())
    val detailScreenState : StateFlow<DetailScreenState> = _detailScreenState.asStateFlow()

    var isFullScreenDialogDisplay = mutableStateOf(false)
        private set

    private var _fullScreenDialogState = MutableStateFlow(FullScreenDialogState())
    val  fullScreenDialogState : StateFlow<FullScreenDialogState> = _fullScreenDialogState.asStateFlow()

    private var _transactionDetailDialogState = MutableStateFlow(TransactionDetailDialogState())
    val transactionDetailDialogState : StateFlow<TransactionDetailDialogState> = _transactionDetailDialogState.asStateFlow()

    var amountTextFieldState = mutableStateOf("")
        private set

    init {
        getCustomer()
       viewModelScope.launch {
            getTransactions()
        }
    }

    fun getCustomer() {
        viewModelScope.launch {
            repository.getCustomer(customerId).collect {
                if (it != null) {
//                    Log.d("DetailView", "customer DB changed")
                    _topBarCustomerDetail.value = it.toTopBarCustomerDetail()
                }
            }
        }
    }


     suspend fun getTransactions() {
             repository.getTransactions(customerId).collect { transactions ->
                 Log.d("DetailView", "transactionTableUpdated")
                 _detailScreenState.update {
                     it.copy(
                         transactions = transactions.map {
                             it.toCustomerTransactionDisplay()
                         }
                     )
                 }
             }
    }

    fun onAmountSaveButtonClick() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy - hh:mm a", Locale.getDefault())
        val timeStamp = dateFormat.format(Date())

        if(istextFieldAmountValid(amountTextFieldState.value)){
            _fullScreenDialogState.update {
                it.copy(
                    isTeaxtFieldError = true
                )
            }
        }else {
            viewModelScope.launch {
                repository.insertTransaction(
                    CustomerTransaction(
                        customerId = customerId,
                        transactionValue = findTransactionValue(amountTextFieldState.value),
                        timeStamp = timeStamp
                    )
                )
                updateCustomerTransactionInCustomerTable(customerRepository = repository,customerId = customerId)
//                repository.getCustomerFinalAmount(customerId = customerId)
//                    .collect { customerFinalAmount ->
//                        Log.d("Detaiscreen",customerFinalAmount.closingBalance.toString())
//                        repository.updateCustomerFinalAmount(
//                            customerId = customerFinalAmount.customerId,
//                            gaveRupee = customerFinalAmount.totalGaveAmount,
//                            gotRupee = customerFinalAmount.totalGotAmount,
//                            closingBalance = customerFinalAmount.closingBalance
//                        )
//
//                    }
            }
            isFullScreenDialogDisplay.value = false
            _fullScreenDialogState.update {
                it.copy(
                    isTeaxtFieldError = false,
                )
            }

        }
    }

    fun istextFieldAmountValid(value : String) : Boolean {
        try {
            value.toDouble()
        }catch (e : NumberFormatException){
            return true
        }
        return (value.isEmpty() || !(value.toDouble() > 0 || value.toDouble() < 0) || value.toDouble() > 9999999.00)
    }

    fun findTransactionValue(amount : String) : Double{
        if(fullScreenDialogState.value.transactionAction == TransactionAction.MoneyGot){
            return  "%.2f".format(amount.toDouble()).toDouble()
        }else{
            return  "%.2f".format("-$amount".toDouble()).toDouble()
        }
//        val formattedAmount = if (fullScreenDialogState.value.transactionAction == TransactionAction.MoneyGot) {
//            amount
//        } else {
//            "-$amount"
//        }
//
//        val decimalValue = BigDecimal(formattedAmount)
//        val decimalFormat = DecimalFormat("#.##") // Adjust the number of decimal places as needed
//        val formattedDouble = decimalFormat.format(decimalValue).toDouble()
//
//        return formattedDouble
    }

    fun onActionButtonClick(transactionAction: TransactionAction) {
        isFullScreenDialogDisplay.value = true
        _fullScreenDialogState.update {
            it.copy(
                transactionAction = transactionAction,

            )
        }
        amountTextFieldState.value = ""
        }


    fun onCloseButtonClick() {
        isFullScreenDialogDisplay.value = false
        _fullScreenDialogState.update {
            it.copy(
                isTeaxtFieldError = false
            )
        }
    }

    fun amountTextFieldValueChange(amount : String) {
        amountTextFieldState.value = amount
    }

    fun onTransactionItemClick(transaction : CustomerTransactionDisplay) {
        _transactionDetailDialogState.update {
            it.copy(
                customerId = transaction.customerId,
                transactionId = transaction.transactionId,
                isTransactionDetailDialogOpen = true,
                transactionsValue = transaction.transactionValue,
                transactionArrowIcon = transaction.transactionArrowIcon,
                timeStamp = transaction.timeStamp,
                amountDisplayColor = transaction.color
            )
        }
    }

    fun onTransactionDialogDeleteButtonClick(transactionId : Int) {
        viewModelScope.launch {
            repository.deleteTransactionById(transactionId)
            updateCustomerTransactionInCustomerTable(repository,customerId)
        }
        onTransactionDetailDialogClose()
    }

    fun onTransactionDetailDialogClose() {
        _transactionDetailDialogState.update {
            it.copy(
                isTransactionDetailDialogOpen = false
            )
        }
    }
}

fun Customer.toTopBarCustomerDetail() = TopBarCustomerDetail(
    customerId = customerId,
    customerName = name,
    closingBalance = findFormattedAmount(closingBalance),
    closingBalanceColor = findBalanceColor(closingBalance)
)

fun CustomerFinalAmount.toCustomer() = Customer(
    customerId = customerId,
    gotRupee = totalGotAmount,
    gaveRupee = totalGaveAmount,
    closingBalance = closingBalance
)

fun CustomerTransaction.toCustomerTransactionDisplay() : CustomerTransactionDisplay = CustomerTransactionDisplay(
    transactionId = transactionId,
    customerId = customerId,
    transactionValue = findFormattedAmount(transactionValue),
    transactionArrowIcon = if(transactionValue > 0.0){TransactionArrowIcon.ArrowDown}else if(transactionValue < 0.0){TransactionArrowIcon.ArrowUp}else{TransactionArrowIcon.Default},
    timeStamp = timeStamp,
    color = findAmountDisplayColor(transactionValue)
)

fun findAmountDisplayColor(amount : Double) : AmountColor{
    if(amount > 0){
        return AmountColor.Green
    }else{
        return AmountColor.Red
    }
}

data class TransactionDetailDialogState(
    val customerId: Int = 0,
    val transactionId: Int = 0,
    val isTransactionDetailDialogOpen : Boolean = false,
    val transactionsValue : String = "",
    val transactionArrowIcon: TransactionArrowIcon = TransactionArrowIcon.Default,
    val timeStamp: String = "",
    val amountDisplayColor: AmountColor = AmountColor.Default
)

data class FullScreenDialogState(
    val transactionAction: TransactionAction = TransactionAction.Default,
    val isTeaxtFieldError : Boolean = false
)

data class DetailScreenState(
    val transactions : List<CustomerTransactionDisplay> = listOf()
)

data class CustomerTransactionDisplay(
    val transactionId : Int = 0,
    val customerId : Int = 0,
    val transactionValue : String,
    val transactionArrowIcon : TransactionArrowIcon = TransactionArrowIcon.Default,
    val timeStamp : String = "",
    val color : AmountColor = AmountColor.Default
)

sealed class TransactionArrowIcon(val icon: Int){
    object ArrowDown : TransactionArrowIcon(R.drawable.arrow_down)
    object ArrowUp : TransactionArrowIcon(R.drawable.arrow_up)
    object Default : TransactionArrowIcon(R.drawable.dot)
}

data class TopBarCustomerDetail(
    val customerId: Int = 0,
    val customerName : String = "",
    val closingBalance : String = "",
    val closingBalanceColor: Color = Color.Unspecified
)

sealed class AmountColor(val color : Color){
    object Green : AmountColor(color = closing_balance_green)
    object Red : AmountColor(color = closing_balance_red)
    object Default : AmountColor(color = Color.Unspecified)
}

sealed class TransactionAction(val color: Color,val name : String){
    object MoneyGave : TransactionAction(color = Color.Red, name = "You Gave")
    object  MoneyGot : TransactionAction(color = amount_dialog_title_green, name = "You Got")
    object Default : TransactionAction(color = Color.Black, name = "")
}
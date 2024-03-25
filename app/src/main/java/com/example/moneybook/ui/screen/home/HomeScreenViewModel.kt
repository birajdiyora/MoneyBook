package com.example.moneybook.ui.screen.home

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneybook.data.Customer
import com.example.moneybook.data.CustomerRepository
import com.example.moneybook.data.CustomerTransaction
import com.example.woof.ui.theme.closing_balance_green
import com.example.woof.ui.theme.closing_balance_red
import com.example.woof.ui.theme.md_theme_dark_background
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class HomeScreenViewModel @Inject constructor(val repository: CustomerRepository) : ViewModel() {

//    val homeUiState : MutableStateFlow<HomeUiState> =
//        repository.getcustomers().map {
//            HomeUiState(it)
//        }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000L), initialValue = HomeUiState())

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState : StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCustomers()
        }
    }

    suspend fun getCustomers() {
//        Log.d("HomeScreen1","customers.toString()")
        repository.getcustomers().collect{customers ->
           _homeUiState.update {
               it.copy(
                   customerList = customers.map {
                       it.toCustomerDetail()
                   }
               )
           }
        }

    }

    fun onCloseDialog(){
       _homeUiState.update {
            it.copy(
                isShowDialog = false,
                customerName = "",
                isCustomerNameError = CustomerNameDialogTextFiledError.NoError
            )
       }
    }
    fun onShowDialog() {
        _homeUiState.update {
            it.copy(
                isShowDialog = true
            )
        }
    }
    fun updateUiState(name : String) {
       _homeUiState.update {
           it.copy(
               customerName = name
           )
       }
    }

    fun onSaveCustomer() {
        val errorCheck = isValidInput(homeUiState.value.customerName)
        if(errorCheck == CustomerNameDialogTextFiledError.NoError){
            _homeUiState.update { it.copy(isCustomerNameError = CustomerNameDialogTextFiledError.NoError,
                isShowDialog = false) }
            viewModelScope.launch {
                repository.insert(homeUiState.value.toCustomer())
            }
            _homeUiState.update {
                it.copy(
                    customerName = ""
                )
            }
        }else{
            _homeUiState.update { it.copy(isCustomerNameError = errorCheck) }
        }
    }

}
fun isValidInput(name: String) : CustomerNameDialogTextFiledError {
    if(name.isNotBlank() && name.length <= 15){
        return CustomerNameDialogTextFiledError.NoError
    }else if(name.length > 15){
        return CustomerNameDialogTextFiledError.Max15Line
    }else{
        return CustomerNameDialogTextFiledError.Blank
    }
}

fun Customer.toCustomerDetail() = CustomerDetail(
    id = customerId,
    customerName = name,
    closeBalance = findFormattedAmount(closingBalance),
    balanceColor = findBalanceColor(closingBalance)
)

fun findBalanceColor(closeBalnce : Double) : Color{
    if(closeBalnce > 0)
        return closing_balance_green
    else if(closeBalnce < 0)
        return closing_balance_red
    else
        return Color.Black
}

fun findFormattedAmount(amount: Double): String {
    val amountWithCommas = formatAmountWithCommas(amount)
    Log.d("HomeScreenViewModel",amountWithCommas)
    val rupeeSymbol = '\u20B9'
    val formattedAmount = String.format(Locale.getDefault(), "%c %s", rupeeSymbol, amountWithCommas)
    return formattedAmount
}

fun formatAmountWithCommas(amount: Double): String {
//    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
//    return numberFormat.format(amount)
    // Initialize the formatted amount string.
//   return DecimalFormat("##,##,##0").format(amount)
    val symbols = DecimalFormatSymbols(Locale.getDefault())
    symbols.groupingSeparator = ','

    val format = DecimalFormat("#,##,##0.###", symbols)
    return format.format(amount)

}

fun HomeUiState.toCustomer() : Customer = Customer(
    name = customerName
)

data class HomeUiState(
    val customerList : List<CustomerDetail> = listOf(),
    val isShowDialog : Boolean = false,
    val isCustomerNameError : CustomerNameDialogTextFiledError = CustomerNameDialogTextFiledError.NoError,
    val customerName : String = ""
    )

data class CustomerDetail(
    val id : Int = 0,
    val customerName : String = "",
    val closeBalance : String = "",
    val balanceColor : Color = Color.Black
)

sealed class CustomerNameDialogTextFiledError(val error : String){
    object Blank : CustomerNameDialogTextFiledError(error = "Enter customer name")
    object Max15Line : CustomerNameDialogTextFiledError(error = "Max 15 line allow")
    object NoError : CustomerNameDialogTextFiledError(error = "")
}

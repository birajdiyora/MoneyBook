package com.example.moneybook.ui.screen.CustomerProfile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneybook.data.Customer
import com.example.moneybook.data.CustomerRepository
import com.example.moneybook.ui.screen.home.CustomerNameDialogTextFiledError
import com.example.moneybook.ui.screen.home.findFormattedAmount
import com.example.moneybook.ui.screen.home.isValidInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerProfileScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val repository: CustomerRepository
) : ViewModel() {
    val customerId : Int = checkNotNull(savedStateHandle["customer_id"])

    private var _editProfileUiState = MutableStateFlow(EditProfleUiState())
    val editProfleUiState: StateFlow<EditProfleUiState> = _editProfileUiState.asStateFlow()

    private var _amountDisplayUiState = MutableStateFlow(AmountDisplayUiState())
    val amountDisplayUiState: StateFlow<AmountDisplayUiState> = _amountDisplayUiState.asStateFlow()

    private var _deleteCustomerUiState = MutableStateFlow(DeleteCustomerUiState())
    val deleteCustomerUiState: StateFlow<DeleteCustomerUiState> = _deleteCustomerUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCustomerDetail(customerId)
        }
    }

    suspend fun getCustomerDetail(customerId : Int) {
        repository.getCustomer(customerId).collect{customer ->
            if (customer != null) {
                _editProfileUiState.update {
                    it.copy(
                        customerName = customer.name ?: ""
                    )
                }
                _amountDisplayUiState.update {
                    it.copy(
                        amountReceived = findFormattedAmount(customer.gotRupee),
                        amountGiven = findFormattedAmount(customer.gaveRupee),
                        closingBalance = findFormattedAmount(customer.closingBalance)
                    )
                }
            }
        }
    }

    fun onOpenEditNamefullScreenDialog(customerName: String) {
        _editProfileUiState.update {
            it.copy(
                isEditNameDialogOpen = true,
                editNameTextFieldValue = customerName
            )
        }
    }

    fun onEditNameFullScreenDialogClose() {
        _editProfileUiState.update {
            it.copy(
                isEditNameDialogOpen = false,
                editNameTextFieldValue = "",
                editNameTextFieldError = CustomerNameDialogTextFiledError.NoError
            )
        }
    }

    fun onEditNameTextFieldValueChange(name : String) {
        _editProfileUiState.update {
            it.copy(
                editNameTextFieldValue = name
            )
        }
    }

    fun onSaveCustomerName(customerName: String) {
        val error = isValidInput(customerName)

        if(error == CustomerNameDialogTextFiledError.NoError) {
            viewModelScope.launch {
                repository.updateCustomerName(
                    customerId = customerId,
                    customerName = customerName
                )
            }
            onEditNameFullScreenDialogClose()
        }else{
            _editProfileUiState.update {
                it.copy(
                    editNameTextFieldError = error
                )
            }
        }
    }

    fun isCustomerNameIsValid(name: String) : Boolean {
        return !name.isBlank()
    }

    fun onDeleteCustomer() {
        viewModelScope.launch {
            repository.deleteCustomer(customerId = customerId)
        }
        onDeleteCustomerDialogClose()
    }

    fun onDeleteCustomerDialogOpen() {
       _deleteCustomerUiState.update {
           it.copy(
               isDeleteCustomerDialogOpen = true
           )
       }
    }

    fun onDeleteCustomerDialogClose() {
        _deleteCustomerUiState.update {
            it.copy(
                isDeleteCustomerDialogOpen = false
            )
        }
    }
}

data class EditProfleUiState(
    val customerName : String = "",
    val isEditNameDialogOpen : Boolean = false,
    val editNameTextFieldValue : String = "",
    val editNameTextFieldError : CustomerNameDialogTextFiledError = CustomerNameDialogTextFiledError.NoError
)

data class AmountDisplayUiState(
    val amountReceived : String = "",
    val amountGiven : String = "",
    val closingBalance : String = ""
)

data class DeleteCustomerUiState(
    val isDeleteCustomerDialogOpen : Boolean = false
)
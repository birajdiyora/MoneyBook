package com.example.moneybook.ui

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moneybook.MoneyBookApplication
import com.example.moneybook.ui.screen.CustomerProfile.CustomerProfileScreenViewModel
import com.example.moneybook.ui.screen.detail.DetailScreenViewModel
import com.example.moneybook.ui.screen.home.HomeScreenViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
//        initializer {
//            Log.e("MoneyApplication","hello1")
//            HomeScreenViewModel(inventoryApplication().container.customerRepository)
//        }
//        initializer {
//            DetailScreenViewModel(
//                this.createSavedStateHandle(),
//                inventoryApplication().container.customerRepository
//            )
//        }
//        initializer {
//            CustomerProfileScreenViewModel(
//                this.createSavedStateHandle(),
//                inventoryApplication().container.customerRepository
//            )
//        }
    }
}

fun CreationExtras.inventoryApplication(): MoneyBookApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MoneyBookApplication)
package com.example.moneybook

import android.app.Application
import android.util.Log
import com.example.moneybook.data.AppContainer
import com.example.moneybook.data.AppdataContainer
import com.example.moneybook.data.CustomerRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@HiltAndroidApp
class MoneyBookApplication : Application() {

    lateinit var container: AppContainer

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        container = AppdataContainer(this)

    }
}

suspend fun updateCustomerTransactionInCustomerTable(customerRepository: CustomerRepository,customerId : Int) {
    customerRepository.getCustomerFinalAmount(customerId).collect{
        customerRepository.updateCustomerFinalAmount(
            customerId = customerId,
            gaveRupee = "%.2f".format(it.totalGaveAmount).toDouble(),
            gotRupee = "%.2f".format(it.totalGotAmount).toDouble(),
            closingBalance = "%.2f".format(it.closingBalance).toDouble()
        )
    }
}



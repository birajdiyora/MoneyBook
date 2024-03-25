package com.example.moneybook.data

import android.content.Context
import android.util.Log

interface AppContainer {
    val customerRepository : CustomerRepository
}

class AppdataContainer(private val context : Context) : AppContainer{

    override val customerRepository: CustomerRepository by lazy {
        OfflineCustomerrepository(
            CustomerDatabase.getDatabase(context).getCustomerDao(),
            CustomerDatabase.getDatabase(context).getCustomerTransactionDao()
        )
    }

}



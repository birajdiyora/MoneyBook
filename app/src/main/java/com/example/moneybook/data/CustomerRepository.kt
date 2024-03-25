package com.example.moneybook.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

     suspend fun insert(customer: Customer)


    suspend fun update(customer: Customer)


     suspend fun delete(customer: Customer)


    fun getCustomer(id : Int) : Flow<Customer>


    fun getcustomers() : Flow<List<Customer>>

    suspend fun insertTransaction(customerTransaction: CustomerTransaction)


    suspend fun updateTransaction(customerTransaction: CustomerTransaction)


    suspend fun deleteTransaction(customerTransaction: CustomerTransaction)


    fun getTransactions(customerId : Int) : Flow<List<CustomerTransaction>>

    fun getCustomerFinalAmount(customerId: Int) : Flow<CustomerFinalAmount>

    suspend fun updateCustomerFinalAmount(customerId: Int,gaveRupee : Double,gotRupee : Double,closingBalance : Double)

    fun getAllTransactions() : Flow<List<CustomerTransaction>>

    suspend fun updateCustomerName(customerName : String,customerId: Int)

    suspend fun deleteCustomer(customerId: Int)

    suspend fun deleteTransactionById(transactionId: Int)
}


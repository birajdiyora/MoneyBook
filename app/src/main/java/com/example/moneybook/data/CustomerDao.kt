package com.example.moneybook.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(customer: Customer)

    @Update
    suspend fun update(customer: Customer)

    @Delete
    suspend fun delete(customer: Customer)

    @Query("SELECT * from customers WHERE customerId = :id")
    fun getCustomer(id : Int) : Flow<Customer>

    @Query("SELECT * from customers order by customerId desc")
    fun getcustomers() : Flow<List<Customer>>

    @Query("UPDATE customers SET gaveRupee = :gaveRupee, gotRupee = :gotRupee, closingBalance = :closingBalance where customerId = :customerId")
    suspend fun updateCustomerFinalAmount(customerId: Int,gaveRupee : Double,gotRupee : Double,closingBalance : Double)

    @Query("UPDATE customers set name = :customerName where customerId = :customerId")
    suspend fun updateCustomerName(customerName : String,customerId: Int)

    @Query("DELETE from customers where customerId = :customerId")
    suspend fun deleteCustomer(customerId: Int)
}

@Dao
interface CustomertransactionDao{

    @Insert
    suspend fun insertTransaction(customerTransaction: CustomerTransaction)

    @Update
    suspend fun updateTransaction(customerTransaction: CustomerTransaction)

    @Delete
    suspend fun deleteTransaction(customerTransaction: CustomerTransaction)

    @Query("SELECT * from customer_transaction WHERE customerId = :customerId order by transactionId desc")
    fun getCustomerTransaction(customerId : Int) : Flow<List<CustomerTransaction>>

    @Query("SELECT customerId," +
            "SUM(CASE WHEN transactionValue > 0.00 THEN transactionValue ELSE 0.00 END) AS totalGotAmount," +
            "SUM(CASE WHEN transactionValue < 0.00 THEN -transactionValue ELSE 0.00 END) AS totalGaveAmount," +
            "SUM(CASE WHEN transactionValue > 0.00 THEN transactionValue ELSE 0.00 END)" +
            " - SUM(CASE WHEN transactionValue < 0.00 THEN -transactionValue ELSE 0.00 END) AS closingBalance" +
            " FROM customer_transaction where customerId = :customerId")
     fun getCustomerFinalAmount(customerId: Int) : Flow<CustomerFinalAmount>

     @Query("SELECT * from customer_transaction")
     fun getAllTransactions() : Flow<List<CustomerTransaction>>

     @Query("DELETE from customer_transaction where transactionId = :transactionId")
     suspend fun deleteTransactionById(transactionId: Int)
}
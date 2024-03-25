package com.example.moneybook.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineCustomerrepository @Inject constructor(
    private val customerDao : CustomerDao,
    private val customertransactionDao: CustomertransactionDao
) : CustomerRepository {
    override suspend fun insert(customer: Customer) {
        return customerDao.insert(customer)
    }

    override suspend fun update(customer: Customer) {
        return customerDao.update(customer)
    }

    override suspend fun delete(customer: Customer) {
        return customerDao.delete(customer)
    }

    override fun getCustomer(id: Int): Flow<Customer> {
        return customerDao.getCustomer(id)
    }

    override fun getcustomers(): Flow<List<Customer>> {
        return customerDao.getcustomers()
    }

    override suspend fun insertTransaction(customerTransaction: CustomerTransaction) {
        return customertransactionDao.insertTransaction(customerTransaction)
    }

    override suspend fun updateTransaction(customerTransaction: CustomerTransaction) {
        return customertransactionDao.updateTransaction(customerTransaction)
    }

    override suspend fun deleteTransaction(customerTransaction: CustomerTransaction) {
        return customertransactionDao.deleteTransaction(customerTransaction)
    }

    override fun getTransactions(customerId: Int): Flow<List<CustomerTransaction>> {
        return customertransactionDao.getCustomerTransaction(customerId)
    }

    override fun getCustomerFinalAmount(customerId: Int): Flow<CustomerFinalAmount> {
        return customertransactionDao.getCustomerFinalAmount(customerId)
    }

    override suspend fun updateCustomerFinalAmount(
        customerId: Int,
        gaveRupee: Double,
        gotRupee: Double,
        closingBalance: Double
    ) {
        return customerDao.updateCustomerFinalAmount(
            customerId = customerId,
            gaveRupee = gaveRupee,
            gotRupee = gotRupee,
            closingBalance = closingBalance
        )
    }

    override fun getAllTransactions(): Flow<List<CustomerTransaction>> {
        return customertransactionDao.getAllTransactions()
    }

    override suspend fun updateCustomerName(customerName: String, customerId: Int) {
       return customerDao.updateCustomerName(customerName = customerName, customerId = customerId)
    }

    override suspend fun deleteCustomer(customerId: Int) {
        return customerDao.deleteCustomer(customerId)
    }

    override suspend fun deleteTransactionById(transactionId: Int) {
        return customertransactionDao.deleteTransactionById(transactionId)
    }

}
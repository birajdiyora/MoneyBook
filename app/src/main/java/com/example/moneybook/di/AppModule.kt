package com.example.moneybook.di

import android.content.Context
import androidx.room.Room
import com.example.moneybook.data.CustomerDao
import com.example.moneybook.data.CustomerDatabase
import com.example.moneybook.data.CustomerRepository
import com.example.moneybook.data.CustomertransactionDao
import com.example.moneybook.data.OfflineCustomerrepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun getCustomerDao(customerDatabase: CustomerDatabase) : CustomerDao = customerDatabase.getCustomerDao()

    @Singleton
    @Provides
    fun getCustomerTransactionDao(customerDatabase: CustomerDatabase) : CustomertransactionDao = customerDatabase.getCustomerTransactionDao()

    @Singleton
    @Provides
    fun getCustomerDatabase(@ApplicationContext context : Context) : CustomerDatabase =
        Room.databaseBuilder(context, CustomerDatabase::class.java,"customer_database")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun getCustomerRepository(customerDao: CustomerDao,customertransactionDao: CustomertransactionDao) : CustomerRepository{
        return OfflineCustomerrepository(customerDao,customertransactionDao)
    }

}
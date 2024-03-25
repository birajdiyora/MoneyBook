package com.example.moneybook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Customer::class,CustomerTransaction::class], version = 1, exportSchema = false)
abstract class CustomerDatabase : RoomDatabase() {
    abstract fun getCustomerDao() : CustomerDao
    abstract fun getCustomerTransactionDao() : CustomertransactionDao

    companion object{
        @Volatile
        private var Instance : CustomerDatabase? = null
        fun getDatabase(context: Context) : CustomerDatabase{
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, CustomerDatabase::class.java,"customer_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
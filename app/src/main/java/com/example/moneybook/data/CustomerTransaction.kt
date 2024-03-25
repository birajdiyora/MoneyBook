package com.example.moneybook.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.util.TableInfo

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["customerId"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    tableName = "customer_transaction"
)
data class CustomerTransaction (
    @PrimaryKey(autoGenerate = true)
    val transactionId : Int = 0,
    val customerId : Int = 0,
    val transactionValue : Double,
    val timeStamp : String = ""
)
package com.example.moneybook.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val customerId : Int = 0,
    val name : String = "",
    val gaveRupee : Double = 0.0,
    val gotRupee : Double = 0.0,
    val closingBalance : Double = 0.0
)
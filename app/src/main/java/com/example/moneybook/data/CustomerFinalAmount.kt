package com.example.moneybook.data

data class CustomerFinalAmount(
    val customerId : Int,
    val totalGotAmount : Double,
    val totalGaveAmount : Double,
    val closingBalance : Double
)

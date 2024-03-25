package com.example.moneybook.navigation

import androidx.annotation.StringRes
import com.example.moneybook.R

sealed class MoneyBookScreen(
    val title : String,
    val route : String
) {
    object HomeScreen : MoneyBookScreen(title = "Money Book", route = "home_screen")
    object DetailScreen : MoneyBookScreen(title = "Customer Detail", route = "customer_detail")
    object EditCustomerScreen : MoneyBookScreen(title = "Edit Name", route = "edit_customer")
    object CustomerProfilescreen : MoneyBookScreen(title = "Cutomer Profile", route = "customer_profile")
}


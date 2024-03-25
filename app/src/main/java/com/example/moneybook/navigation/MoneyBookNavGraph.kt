package com.example.moneybook.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.moneybook.data.Customer
import com.example.moneybook.ui.screen.CustomerProfile.CustomerProfileScreen
import com.example.moneybook.ui.screen.EditCustomerScreen
import com.example.moneybook.ui.screen.detail.DetailScreen
import com.example.moneybook.ui.screen.home.CustomerDetail
import com.example.moneybook.ui.screen.home.HomeScreen

@Composable
fun MoneyBookNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MoneyBookScreen.HomeScreen.route
    ){
        composable(route = MoneyBookScreen.HomeScreen.route){
            HomeScreen(
                navigateToDetailScreen = {
                    navController.navigate("${MoneyBookScreen.DetailScreen.route}/${it.id}/${it.customerName}")
                }
            )
        }
        composable(route = "${MoneyBookScreen.DetailScreen.route}/{customer_id}/{customer_name}",
            arguments = listOf(
                navArgument("customer_id"){
                    type = NavType.IntType
                },
                navArgument("customer_name"){
                    type = NavType.StringType
                }

            )
        ){
            DetailScreen(
                onNavigationBack = {
                    navController.navigateUp()
                },
                onCustomerProfileClick = {
                    navController.navigate("${MoneyBookScreen.CustomerProfilescreen.route}/${it}")
                }
            )
        }
        composable(route = MoneyBookScreen.EditCustomerScreen.route){
            EditCustomerScreen()
        }
        composable(route = "${MoneyBookScreen.CustomerProfilescreen.route}/{customer_id}",
            arguments = listOf(
                navArgument("customer_id"){
                    type = NavType.IntType
                }
            )
        ){
            CustomerProfileScreen(
                onNavigationBack = {
                    navController.navigateUp()
                },
                onDeleteNavBackToHome = {
//                    navController.navigate(MoneyBookScreen.HomeScreen.route)
                    navController.popBackStack(MoneyBookScreen.HomeScreen.route, inclusive = false)
                }
            )
        }
    }
}

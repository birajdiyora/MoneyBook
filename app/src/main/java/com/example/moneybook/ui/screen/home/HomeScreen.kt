package com.example.moneybook.ui.screen.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.ResourceFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneybook.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneybook.CustomerCenterAlignedTopAppBar
import com.example.moneybook.ui.AppViewModelProvider
import com.example.moneybook.ui.theme.Montserrat
import com.example.woof.ui.theme.closing_balance_green
import com.example.woof.ui.theme.closing_balance_red
import com.example.woof.ui.theme.closing_balance_red_dark_theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToDetailScreen: (CustomerDetail) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()
    val homeUiState by viewModel.homeUiState.collectAsState()
    Log.d("HomeScreen",homeUiState.customerList.toString())
//    var isShowDialog by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        CustomerCenterAlignedTopAppBar(
            title = stringResource(id = R.string.app_title)
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = { viewModel.onShowDialog() }, shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.Default.Add, contentDescription = null
            )
        }
    }) { paddingValues ->
        HomeBody(
            modifier = Modifier.padding(paddingValues),
            customers = homeUiState.customerList,
            clickOnCustomer = navigateToDetailScreen
        )

        if (homeUiState.isShowDialog) {
            CustomerDialogBox(
                homeUiState = homeUiState,
                onCloseDialog = { viewModel.onCloseDialog() },
                onCustomerNameChange = { viewModel.updateUiState(it)},
                onSaveButtonClick = { viewModel.onSaveCustomer()}
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeBody(
    clickOnCustomer : (CustomerDetail) -> Unit,
    modifier: Modifier, customers: List<CustomerDetail>
) {
    Column(
        modifier = modifier
    ) {
        if (customers.isEmpty()) CustomerEmpty(modifier = Modifier.fillMaxSize())
        else {
            CustomerList(
                modifier = Modifier,
                customers = customers,
                clickOnCustomer = clickOnCustomer
            )
        }
    }
}

@Composable
fun CustomerList(
    clickOnCustomer : (CustomerDetail) -> Unit,
    modifier: Modifier, customers: List<CustomerDetail>
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(customers) { customer ->
            CustomerListItem(
                customer = customer, modifier = Modifier.padding(5.dp),
                clickOnCustomer = clickOnCustomer
            )
        }
    }
}

@Composable
fun CustomerListItem(
    clickOnCustomer : (CustomerDetail) -> Unit,
    customer: CustomerDetail, modifier: Modifier
) {
    val closeBalanceColor : Color = if(isSystemInDarkTheme()){
//        Color(0xFFFFFFFF)
        if(customer.balanceColor == closing_balance_red){
           closing_balance_red_dark_theme
        }else if(customer.balanceColor == closing_balance_green){
            customer.balanceColor
        }else{
            Color(0xFFFFFFFF)
        }
    }else{
        customer.balanceColor
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { clickOnCustomer(customer) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.65f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.customer_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .size(37.dp)
                        .background(Color.White),
                )
                Text(
                    text = customer.customerName,
//                style = MaterialTheme.typography.headlineSmall,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(start = 8.dp),
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
//            Spacer(modifier = Modifier.weight(1f))

            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.35f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = customer.closeBalance,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = closeBalanceColor,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
            }
        }
    }
}

@Composable
fun CustomerEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_customer),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp),
            colorFilter = if(isSystemInDarkTheme()){ColorFilter.tint(Color.White)}else{ ColorFilter.tint(Color.Black)}
        )
        Text(
            text = "No any customer !",
            fontWeight = FontWeight.W600
        )
        Text(
            text = "Please add customer",
            fontWeight = FontWeight.W600
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDialogBox(
    homeUiState: HomeUiState,
    onCloseDialog: () -> Unit,
    onCustomerNameChange : (String) -> Unit,
    onSaveButtonClick : () -> Unit
) {

    AlertDialog(
        onDismissRequest = { onCloseDialog() },
        confirmButton = {
                        Button(
                            onClick = onSaveButtonClick ,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Save")
                        }
                        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Add Customer")
                IconButton(onClick = { onCloseDialog() }) {
                    Icon(
                        imageVector = Icons.Default.Close, contentDescription = null
                    )
                }
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = homeUiState.customerName,
                    onValueChange = onCustomerNameChange,
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(text = "Name") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                    ),
                    placeholder = {
                        Text(
                            text = "Customer Name",
                        )
                    },
                    supportingText = {
                                     Text(text = homeUiState.isCustomerNameError.error)
                    },
                    isError = homeUiState.isCustomerNameError != CustomerNameDialogTextFiledError.NoError,
                    maxLines = 1,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {


}

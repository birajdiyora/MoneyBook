package com.example.moneybook.ui.screen.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.text.input.OffsetMapping
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneybook.R
import com.example.moneybook.ui.AppViewModelProvider
import com.example.woof.ui.theme.closing_balance_green
import com.example.woof.ui.theme.closing_balance_red
import com.example.woof.ui.theme.closing_balance_red_dark_theme
import dagger.hilt.android.lifecycle.HiltViewModel

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
//    viewModel: DetailScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onCustomerProfileClick : (name : Int) -> Unit,
    onNavigationBack : () -> Unit
) {
    val viewModel= hiltViewModel<DetailScreenViewModel>()
    val dialogState by viewModel.fullScreenDialogState.collectAsState()
    val detailScreenState by viewModel.detailScreenState.collectAsState()
    val topBarCustomerDetail by viewModel.topBarCustomerDetail.collectAsState()
    val transactionDetailDialog by viewModel.transactionDetailDialogState.collectAsState()

    val closeBalanceColor : Color = if(isSystemInDarkTheme()){
//        Color(0xFFFFFFFF)
        if(topBarCustomerDetail.closingBalanceColor == closing_balance_red){
            closing_balance_red_dark_theme
        }else if(topBarCustomerDetail.closingBalanceColor == closing_balance_green){
            topBarCustomerDetail.closingBalanceColor
        }else{
            Color(0xFFFFFFFF)
        }
    }else{
        topBarCustomerDetail.closingBalanceColor
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MediumTopAppBar(
                    title = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
//                                    .background(MaterialTheme.colorScheme.secondaryContainer),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Balance",
                                    style = MaterialTheme.typography.displayMedium
                                    )
                                Text(
                                    text = topBarCustomerDetail.closingBalance,
                                    style = MaterialTheme.typography.displayMedium,
                                    modifier = Modifier.padding(end = 12.dp),
                                    color = closeBalanceColor,
                                    overflow = TextOverflow.Ellipsis,
                                    softWrap = false,
                                )
                            }
//                            Row(
//                                modifier = Modifier
//                                    .clickable {
//                                        onCustomerProfileClick(topBarCustomerDetail.customerId)
//                                    },
////                                horizontalArrangement = Arrangement.Center,
////                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Image(
//                                    painter = painterResource(id = R.drawable.customer_icon),
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .clip(MaterialTheme.shapes.small)
//                                        .size(45.dp)
//                                        .background(Color.White),
//                                )
//                                Text(
//                                    text = topBarCustomerDetail.customerName,
//                                    style = MaterialTheme.typography.displayMedium,
//                                    modifier = Modifier
//                                        .padding(start = 8.dp)
//                                )
//                                Spacer(modifier = Modifier.weight(1f))
//                                Text(
//                                    text = topBarCustomerDetail.closingBalance,
//                                    style = MaterialTheme.typography.displayMedium,
//                                    modifier = Modifier.padding(end = 8.dp),
//                                    color = closeBalanceColor
//
//                                )
//                            }

                },
                    navigationIcon = {
                        Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        onCustomerProfileClick(topBarCustomerDetail.customerId)
                                    },
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                            IconButton(onClick = onNavigationBack) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                                Image(
                                    painter = painterResource(id = R.drawable.customer_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.small)
                                        .size(45.dp)
                                        .background(Color.White),
                                )
                                Text(
                                    text = topBarCustomerDetail.customerName,
                                    style = MaterialTheme.typography.displayMedium,
                                    modifier = Modifier
                                        .padding(start = 8.dp),
                                    overflow = TextOverflow.Ellipsis,
                                    softWrap = false
                                )
//                                Spacer(modifier = Modifier.weight(1f))
//                                Text(
//                                    text = topBarCustomerDetail.closingBalance,
//                                    style = MaterialTheme.typography.displayMedium,
//                                    modifier = Modifier.padding(end = 8.dp),
//                                    color = closeBalanceColor
//
//                                )
                            }

                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.inverseOnSurface
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                Bottombar(onButtonClick = {
                    viewModel.onActionButtonClick(it)
                })
            }
        ) {paddingValues ->
            DetailBody(
                viewModel = viewModel,
                transactions = detailScreenState.transactions,
                transactionDetailDialog = transactionDetailDialog,
                modifier = Modifier.padding(paddingValues)
            )
            if(viewModel.isFullScreenDialogDisplay.value){
                FullScreenDialog(
                    viewModel = viewModel,
                    dialogState = dialogState
                )
            }

        }
}

@Composable
fun DetailBody(
    viewModel: DetailScreenViewModel,
    transactionDetailDialog : TransactionDetailDialogState,
    transactions : List<CustomerTransactionDisplay>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier){
        if(transactions.isEmpty()){
            TransactionEmpty()
        }else{
            TransactionsList(
                onTransactionItemClick = {
                    viewModel.onTransactionItemClick(it)
                },
                transactions = transactions
            )

            if(transactionDetailDialog.isTransactionDetailDialogOpen){
                TransactionDetailDialog(
                    viewModel = viewModel,
                    transactionDetailDialog = transactionDetailDialog
                )
            }
        }

    }

}

@Composable
fun TransactionsList(
    onTransactionItemClick : (CustomerTransactionDisplay) -> Unit,
    transactions: List<CustomerTransactionDisplay>,
    modifier: Modifier = Modifier
) {
    LazyColumn(){
        items(transactions){
            TransactionListItem(
                transaction = it,
                onTransactionItemClick = onTransactionItemClick
                )
        }
    }
}

@Composable
fun TransactionListItem(
    onTransactionItemClick : (CustomerTransactionDisplay) -> Unit,
    transaction : CustomerTransactionDisplay,
    modifier: Modifier = Modifier
) {
    val balanceColor = if(isSystemInDarkTheme()){
        if(transaction.color.color == closing_balance_red)
            closing_balance_red_dark_theme
        else
            closing_balance_green
    }else{
        transaction.color.color
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable { onTransactionItemClick(transaction) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(
                    text = transaction.transactionValue,
                    style = MaterialTheme.typography.displaySmall,
                    color = balanceColor,
                )
                Icon(
                    painter = painterResource(id = transaction.transactionArrowIcon.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp),
                    tint = balanceColor
                )
            }
            Text(
                text = transaction.timeStamp,
                style = MaterialTheme.typography.titleSmall
                )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun FullScreenDialog(
    viewModel: DetailScreenViewModel,
    dialogState: FullScreenDialogState,
    modifier: Modifier = Modifier
) {

        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    topBar = {
                      TopBarForDialog(
                          viewModel = viewModel,
                         dialogState = dialogState,
                          onCloseDialog = { viewModel.onCloseButtonClick()}
                      )
                    }
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(it)) {
                       OutlinedTextField(
                           value = viewModel.amountTextFieldState.value,
                           onValueChange = {
                                   viewModel.amountTextFieldValueChange(it)
                           },
                           colors = TextFieldDefaults.outlinedTextFieldColors(
                           ),
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(12.dp),
                           placeholder = {
                               Text(
                                   text = "Enter amount",
                                   style = MaterialTheme.typography.titleLarge,
                                   fontWeight = FontWeight.SemiBold)
                           },
                           textStyle = MaterialTheme.typography.titleLarge,
                           leadingIcon = {
                               Icon(painter = painterResource(id = R.drawable.rupee),
                                   contentDescription = null,
                                   modifier = Modifier
                                       .size(30.dp)
                                       .padding(start = 3.dp))
                           },
                           keyboardOptions = KeyboardOptions.Default.copy(
                               keyboardType = KeyboardType.Number
                           ),
                           isError = dialogState.isTeaxtFieldError,
                           singleLine = true,
                           )
                    }
                }
        }
            BackHandler(true) {
                viewModel.onCloseButtonClick()
            }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarForDialog(
    viewModel: DetailScreenViewModel,
    dialogState: FullScreenDialogState,
    onCloseDialog : () -> Unit
) {
    TopAppBar(
        title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dialogState.transactionAction.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = dialogState.transactionAction.color,
                        fontWeight = FontWeight.SemiBold
                    )
                    Button(onClick = { viewModel.onAmountSaveButtonClick() }) {
                        Text(text = "Save")
                    }
                }
        },
        navigationIcon = {
            IconButton(
                onClick = onCloseDialog,
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = dialogState.transactionAction.color
                )
            }
        }
        )
}

@Composable
fun Bottombar(
    onButtonClick : (actionPerform : TransactionAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Button(
                onClick = { onButtonClick(TransactionAction.MoneyGave) },
                modifier = Modifier
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDA2222)
                )
            ) {
                Text(
                    text = "You Gave",
                    style = MaterialTheme.typography.titleMedium
                    )
            }
            Button(
                onClick = {
                          onButtonClick(TransactionAction.MoneyGot)
                },
                modifier = Modifier
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF087E0D)
                )
            ) {
                Text(
                    text = "You Got",
                    style = MaterialTheme.typography.titleMedium
                    )
            }
        }
    }
}

@Composable
fun TransactionDetailDialog(
    viewModel: DetailScreenViewModel,
    transactionDetailDialog : TransactionDetailDialogState,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { viewModel.onTransactionDetailDialogClose() },
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Detail",
                    fontWeight = FontWeight.W400,
                    fontSize = 35.sp
                    )
                IconButton(onClick = { viewModel.onTransactionDetailDialogClose() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = transactionDetailDialog.transactionsValue,
                        style = MaterialTheme.typography.displayMedium,
                        color = transactionDetailDialog.amountDisplayColor.color,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        painter = painterResource(id = transactionDetailDialog.transactionArrowIcon.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp),
                        tint = transactionDetailDialog.amountDisplayColor.color
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = transactionDetailDialog.timeStamp,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.W500
                    )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { viewModel.onTransactionDialogDeleteButtonClick(transactionDetailDialog.transactionId) },
                    modifier = Modifier
                        .weight(1f)
                    ) {
                    Text(text = "Delete")
                }
                Spacer(modifier = Modifier.width(15.dp))
                Button(
                    onClick = { viewModel.onTransactionDetailDialogClose() },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = "Close")
                }
            }
                        
        },
    )
}

@Composable
fun TransactionEmpty(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_transaction),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp),
            colorFilter =
            if(isSystemInDarkTheme()) {
                ColorFilter.tint(Color.White)
            }else{ColorFilter.tint(
                Color.Black)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "No any transaction !", fontWeight = FontWeight.W600)
    }
}


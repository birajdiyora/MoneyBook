package com.example.moneybook.ui.screen.CustomerProfile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneybook.CustomerTopAppBar
import com.example.moneybook.R
import com.example.moneybook.ui.AppViewModelProvider
import com.example.moneybook.ui.screen.detail.TopBarForDialog
import com.example.moneybook.ui.screen.home.CustomerNameDialogTextFiledError
import java.security.AllPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerProfileScreen(
    onNavigationBack : () -> Unit,
    onDeleteNavBackToHome : () -> Unit
) {
    val viewModel = hiltViewModel<CustomerProfileScreenViewModel>()
    val editProfleUiState by viewModel.editProfleUiState.collectAsState()
    val amountDisplayUiState by viewModel.amountDisplayUiState.collectAsState()
    val deleteCustomerUiState by viewModel.deleteCustomerUiState.collectAsState()

    Scaffold(
        topBar = {
            CustomerTopAppBar(title = "Customer Profile", onNavigationBack = onNavigationBack)
        }
    ) {paddingValues ->
        CustomerProfileBody(
            editProfleUiState = editProfleUiState,
            amountDisplayUiState = amountDisplayUiState,
            deleteCustomerUiState = deleteCustomerUiState,
            viewModel = viewModel,
            onDeleteNavBackToHome = onDeleteNavBackToHome,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun CustomerProfileBody(
    editProfleUiState: EditProfleUiState,
    amountDisplayUiState: AmountDisplayUiState,
    deleteCustomerUiState: DeleteCustomerUiState,
    viewModel: CustomerProfileScreenViewModel,
    onDeleteNavBackToHome : () -> Unit,
    modifier: Modifier = Modifier
) {
    if(editProfleUiState.isEditNameDialogOpen){
        EditNameFullScreenDialog(
            viewModel = viewModel,
            editProfleUiState = editProfleUiState
        )
    }
    if(deleteCustomerUiState.isDeleteCustomerDialogOpen){
        DeleteCustomerDialog(
            onDeleteNavBackToHome = onDeleteNavBackToHome,
            viewModel = viewModel
        )
    }

    Column(
      modifier = modifier
          .fillMaxSize()
          .padding(20.dp)
    ) {
        CustomerProfile(
            viewModel = viewModel,
            editProfleUiState = editProfleUiState
        )
        Spacer(modifier = Modifier.padding(10.dp))
        DisplayTransactionCard(
            amountDisplayUiState = amountDisplayUiState,
            modifier = Modifier
        )
        ActionButton(
            viewModel = viewModel,
            deleteCustomerUiState = deleteCustomerUiState)
    }
}

@Composable
fun CustomerProfile(
    editProfleUiState: EditProfleUiState,
    viewModel: CustomerProfileScreenViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.customer_icon),
            contentDescription = null,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .size(200.dp)
                .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                .padding(20.dp)
            )
        Spacer(modifier = modifier.padding(10.dp))
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
//                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = editProfleUiState.customerName,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    modifier = Modifier,
                )
            }
            Row(
                modifier = Modifier.wrapContentWidth(unbounded = true),
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp, top = 2.dp)
                        .size(18.dp)
                        .clickable {
                            viewModel.onOpenEditNamefullScreenDialog(
                                customerName = editProfleUiState.customerName
                            )
                        },
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
//                Spacer(modifier = Modifier.weight(1f))
            }
        }

    }
}

@Composable
fun DisplayTransactionCard(
    amountDisplayUiState: AmountDisplayUiState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Amount Received",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onPrimary
                        )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = amountDisplayUiState.amountReceived,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false,
                        )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(3.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Amount Given",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onPrimary
                        )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = amountDisplayUiState.amountGiven,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false,
                        )
                }
            }
            Divider(thickness = 3.dp)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Closing Balance",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                    )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = amountDisplayUiState.closingBalance,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                )
            }
        }
    }
    
//    Column {
//        Card(
//            modifier = modifier
//                .fillMaxWidth()
//                .height(100.dp),
//            shape = RoundedCornerShape(8.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(
//                    text = "Receive Amount",
//                    style = MaterialTheme.typography.displayMedium
//                )
//                Text(text = "25000")
//            }
//        }
//        Card {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .weight(0.5f),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(
//                    text = "Given Amount",
//                    style = MaterialTheme.typography.displayMedium
//                )
//                Text(text = "25000")
//            }
//        }
//    }
}

@Composable
fun ActionButton(
    deleteCustomerUiState: DeleteCustomerUiState,
    viewModel: CustomerProfileScreenViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Bottom
    ) {
        OutlinedButton(
            onClick = {viewModel.onDeleteCustomerDialogOpen() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            border = BorderStroke(width = 1.dp,color = Color.Red),
            shape = RoundedCornerShape(3.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFF3E2E2)
            )
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Delete Customer",
                    color = Color.Red,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNameFullScreenDialog(
    viewModel: CustomerProfileScreenViewModel,
    editProfleUiState: EditProfleUiState
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
                    TopAppBar(
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Edit Name",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
//                                Button(onClick = {  }) {
//                                    Text(text = "Save")
//                                }
                                IconButton(onClick = { viewModel.onSaveCustomerName(customerName = editProfleUiState.editNameTextFieldValue)}) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { viewModel.onEditNameFullScreenDialogClose() },
                                modifier = Modifier
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                )
                            }
                        }
                    )
                }
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(it)) {
                    OutlinedTextField(
                        value = editProfleUiState.editNameTextFieldValue,
                        onValueChange = {
                            viewModel.onEditNameTextFieldValueChange(it)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        placeholder = {
                            Text(
                                text = "Customer Name",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.W500)
                        },
                        textStyle = MaterialTheme.typography.titleLarge,
                        leadingIcon = {
                            Icon(painter = painterResource(id = R.drawable.customer_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(start = 3.dp))
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text
                        ),
                        isError = editProfleUiState.editNameTextFieldError != CustomerNameDialogTextFiledError.NoError,
                        supportingText = {
                                         Text(text = editProfleUiState.editNameTextFieldError.error)
                        },
                        singleLine = true,
                    )
                }
            }
        }
        BackHandler(true) {
            viewModel.onEditNameFullScreenDialogClose()
        }
    }
}

@Composable
fun DeleteCustomerDialog(
    onDeleteNavBackToHome : () -> Unit,
    viewModel: CustomerProfileScreenViewModel
) {
    AlertDialog(
        onDismissRequest = { viewModel.onDeleteCustomerDialogClose() },
        confirmButton = {
                        TextButton(onClick = {
                            viewModel.onDeleteCustomer()
                            onDeleteNavBackToHome()
                        }) {
                            Text(text = "Confirm")
                        }
                        },
        dismissButton = {
            TextButton(onClick = { viewModel.onDeleteCustomerDialogClose() }) {
                Text(text = "Cancel")
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color.Red
            )
        },
        title = {
            Text(
                text = "Delete customer?",
                fontWeight = FontWeight.W500
                )
        },
        text = {
            Text(
                text = "This will delete customer and there all transactions. After delete you lose all data of this customer.",
                fontWeight = FontWeight.W400
            )
        }
    )
}


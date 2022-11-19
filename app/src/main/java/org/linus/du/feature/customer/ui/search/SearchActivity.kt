package org.linus.du.feature.customer.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import org.linus.core.data.db.entities.Customer
import org.linus.core.ui.theme.Gray300
import org.linus.core.ui.theme.Green
import org.linus.core.ui.theme.Red500
import org.linus.du.R
import org.linus.du.feature.customer.ui.custom_info.CustomerInfoActivity
import org.linus.du.feature.customer.ui.customer_edit.CustomerEditActivity

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            val isDarkMode = isSystemInDarkTheme()
            SideEffect {
                if (isDarkMode) {
                    systemUiController.setStatusBarColor(color = Color.Transparent)
                } else {
                    systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
                }
            }
            SearchContentView(
                viewModel = hiltViewModel(),
                onCheckCustomer = {
                    Intent(this@SearchActivity, CustomerInfoActivity::class.java).apply {
                        putExtra("id", it.id)
                    }.let {
                        startActivity(it)
                    }
                },
                onEditCustomer = {
                    Intent(this@SearchActivity, CustomerEditActivity::class.java).apply {
                        putExtra("id", it.id)
                    }.let {
                        startActivity(it)
                    }
                }
            )
        }
    }
}

@Composable
private fun SearchContentView(
    viewModel: SearchViewModel,
    onCheckCustomer: (Customer) -> Unit,
    onEditCustomer: (Customer) -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val state = viewModel.screenState.collectAsState()
            SearchBox(
                key = state.value.keyword, 
                isError = state.value.isError, 
                onValueChanged = { viewModel.obtainEvent(SearchScreenEvent.SearchKeyInputEvent(it))},
                onSearch = { viewModel.obtainEvent(SearchScreenEvent.SearchConfirmEvent) }
            )
            if (state.value.result.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(items = state.value.result) { customer ->
                        CustomerItemView(
                            customer = customer,
                            onCheckCustomer = onCheckCustomer,
                            onEditCustomer = onEditCustomer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomerItemView(
    customer: Customer,
    onCheckCustomer: (Customer) -> Unit,
    onEditCustomer: (Customer) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                onCheckCustomer.invoke(customer)
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_bookmark),
            contentDescription = null,
            tint = Green,
            modifier = Modifier.size(width = 22.dp, height = 24.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Text(customer.name, style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.width(8.dp))
//                Text("32岁", style = MaterialTheme.typography.body1)
            }
            Text(customer.id, style = MaterialTheme.typography.body2)
        }
        Spacer(modifier = Modifier.padding(8.dp))
        IconButton(
            onClick = { onEditCustomer(customer) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = null,
                tint = Gray300
            )
        }
    }
}

@Composable
private fun SearchBox(
    key: String,
    isError: Boolean,
    onValueChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = key,
        onValueChange = onValueChanged,
        label = { Text("请输入姓名查找") },
        isError = isError,
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            focusedLabelColor = MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.primary,
            errorBorderColor = Red500,
            errorLabelColor = Red500,
            errorCursorColor = Red500
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                onSearch.invoke()
            }
        )
    )
}
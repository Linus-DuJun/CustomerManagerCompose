package org.linus.du.feature.customer.ui.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import org.linus.core.ui.theme.Red500

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
            SearchContentView(viewModel = hiltViewModel())
        }
    }
}

@Composable
private fun SearchContentView(
    viewModel: SearchViewModel
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
            Spacer(modifier = Modifier.padding(16.dp))
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
        modifier = Modifier.fillMaxWidth().padding(16.dp),
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
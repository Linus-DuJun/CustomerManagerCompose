package org.linus.du.feature.customer.ui.add_customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import org.linus.du.CustomerManagerComposeTheme

@AndroidEntryPoint
class AddCustomerActivity: ComponentActivity() {

    val viewModel: AddCustomerViewModel by viewModels()

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

            CustomerManagerComposeTheme {
                AddCustomerScreen(
                    viewModel = viewModel,
                    onBackClick = { finish() }
                )
            }
        }
    }
}
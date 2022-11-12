package org.linus.du.feature.customer.ui.custom_info

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import org.linus.du.CustomerManagerComposeTheme
import org.linus.du.feature.customer.ui.add_customer.AddCustomerScreen

@AndroidEntryPoint
class CustomerInfoActivity : ComponentActivity() {

    val viewModel: CustomerInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCustomerInfo(intent.getStringExtra("id").toString())
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
                CustomerInfoScreen(
                    viewModel = viewModel,
                    onBackClick = { finish() }
                )
            }
        }
    }
}
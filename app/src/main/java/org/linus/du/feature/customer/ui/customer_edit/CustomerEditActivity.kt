package org.linus.du.feature.customer.ui.customer_edit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import org.linus.du.CustomerManagerComposeTheme

@AndroidEntryPoint
class CustomerEditActivity: ComponentActivity() {

    lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getStringExtra("id").toString()
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
                Text("edit id is: $id")
            }
        }
    }
}
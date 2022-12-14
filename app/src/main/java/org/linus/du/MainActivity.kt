package org.linus.du

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import org.linus.core.ui.theme.*
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.backup.ui.BackupActivity
import org.linus.du.feature.customer.ui.add_customer.AddCustomerActivity
import org.linus.du.feature.customer.ui.custom_info.CustomerInfoActivity
import org.linus.du.feature.customer.ui.customer_edit.CustomerEditActivity
import javax.inject.Inject

@Composable
fun CustomerManagerComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = AppTypography,
        shapes = Shapes,
        content = content
    )
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
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
              HomeScreen(
                  onAddCustomer = {
                        startActivity(Intent(this@MainActivity, AddCustomerActivity::class.java))
                  },
                  onBackup = {
                        startActivity(Intent(this@MainActivity, BackupActivity::class.java))
                  },
                  onCheckCustomerInfo = {
                      Intent(this@MainActivity, CustomerInfoActivity::class.java).apply {
                          putExtra("id", it.id)
                      }.let {
                          startActivity(it)
                      }
                  },
                  onEditCustomer = {
                      Intent(this@MainActivity, CustomerEditActivity::class.java).apply {
                          putExtra("id", it.id)
                      }.let {
                          startActivity(it)
                      }
                  }
              )
            }
        }
    }
}


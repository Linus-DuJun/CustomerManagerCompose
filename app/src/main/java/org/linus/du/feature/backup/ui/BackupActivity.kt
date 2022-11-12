package org.linus.du.feature.backup.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import org.linus.core.ui.common.BaseTopAppBar
import org.linus.core.ui.common.LoadingView
import org.linus.core.ui.theme.Ocean300
import org.linus.du.R

@AndroidEntryPoint
class BackupActivity : ComponentActivity() {

    val viewModel: BackupViewModel by viewModels()

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
            Scaffold(
                topBar = { BaseTopAppBar(
                    title = stringResource(id = R.string.backup_title),
                    onBackClick = { finish() })
                }
            ) {
                val state = viewModel.screenState.collectAsState()
                if (state.value.exportSuccess || state.value.importSuccess) {
                    finish()
                }
                BackupContentView(viewModel = hiltViewModel())
            }
        }
    }
}

@Composable
private fun BackupContentView(viewModel: BackupViewModel) {
    val state = viewModel.screenState.collectAsState()
    if (state.value.isExporting || state.value.isImporting) {
        LoadingView()
    }
    AnimatedVisibility(
        visible = !viewModel.screenState.value.isExporting && !viewModel.screenState.value.isImporting
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { viewModel.obtainEvent(BackupScreenEvent.ExportDataEvent) },
                modifier = Modifier.size(width = 180.dp, height = 48.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Ocean300,
                    contentColor = Color.White),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 6.dp,
                    disabledElevation = 4.dp,
                    hoveredElevation = 6.dp,
                    focusedElevation = 6.dp),
                shape = RoundedCornerShape(4.dp),
            ) {
                Text(text = stringResource(id = R.string.backup_export))
            }

            Spacer(modifier = Modifier.height(56.dp))

            Button(
                onClick = { viewModel.obtainEvent(BackupScreenEvent.ImportDataEvent) },
                modifier = Modifier.size(width = 180.dp, height = 48.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Ocean300,
                    contentColor = Color.White),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 6.dp,
                    disabledElevation = 4.dp,
                    hoveredElevation = 6.dp,
                    focusedElevation = 6.dp),
                shape = RoundedCornerShape(4.dp),
            ) {
                Text(text = stringResource(id = R.string.backup_import))
            }
        }
    }
}
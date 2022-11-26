@file:OptIn(ExperimentalPermissionsApi::class)

package org.linus.du.feature.backup.ui

import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DOCUMENTS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
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
        viewModel.setStoragePath(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS))
        setContent {
            val writePermissionState = rememberPermissionState(permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val systemUiController = rememberSystemUiController()
            val isDarkMode = isSystemInDarkTheme()
            SideEffect {
                if (isDarkMode) {
                    systemUiController.setStatusBarColor(color = Color.Transparent)
                } else {
                    systemUiController.setStatusBarColor(color = Color.Transparent, darkIcons = true)
                }
                if (!writePermissionState.status.isGranted) {
                    writePermissionState.launchPermissionRequest()
                }
            }
            Scaffold(
                topBar = { BaseTopAppBar(
                    title = stringResource(id = R.string.backup_title),
                    onBackClick = { finish() })
                }
            ) {
//                val state = viewModel.screenState.collectAsState()
//                if (state.value.exportSuccess || state.value.importSuccess) {
//                    this@BackupActivity.finish()
//                }
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
        visible = true
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.obtainEvent(BackupScreenEvent.ExportCustomerEvent) },
                    modifier = Modifier.size(width = 120.dp, height = 48.dp),
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
                    Text(text = stringResource(id = R.string.backup_customer))
                }

                Button(
                    onClick = { viewModel.obtainEvent(BackupScreenEvent.ExportRecordEvent) },
                    modifier = Modifier.size(width = 120.dp, height = 48.dp),
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
                    Text(text = stringResource(id = R.string.backup_record))
                }

                Button(
                    onClick = { viewModel.obtainEvent(BackupScreenEvent.ExportReturnVisitEvent) },
                    modifier = Modifier.size(width = 120.dp, height = 48.dp),
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
                    Text(text = stringResource(id = R.string.backup_return_visit))
                }
            }

            Spacer(modifier = Modifier.height(56.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.obtainEvent(BackupScreenEvent.ImportCustomerEvent) },
                    modifier = Modifier.size(width = 120.dp, height = 48.dp),
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
                    Text(text = stringResource(id = R.string.restore_customer))
                }

                Button(
                    onClick = { viewModel.obtainEvent(BackupScreenEvent.ImportRecordEvent) },
                    modifier = Modifier.size(width = 120.dp, height = 48.dp),
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
                    Text(text = stringResource(id = R.string.restore_record))
                }

                Button(
                    onClick = { viewModel.obtainEvent(BackupScreenEvent.ImportReturnVisitEvent) },
                    modifier = Modifier.size(width = 120.dp, height = 48.dp),
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
                    Text(text = stringResource(id = R.string.restore_return_visit))
                }
            }
        }
    }
}
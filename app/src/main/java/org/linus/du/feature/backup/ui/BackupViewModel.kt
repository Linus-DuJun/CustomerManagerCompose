package org.linus.du.feature.backup.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.backup.domain.repository.BackupRepository
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val repository: BackupRepository,
    private val toaster: Toaster
): ViewModel() {

    private val _screenState = MutableStateFlow(BackupScreenStateHolder())
    val screenState = _screenState.asStateFlow()

    fun obtainEvent(event: BackupScreenEvent) {
        reduce(event, _screenState.value)
    }

    private fun reduce(event: BackupScreenEvent, currentState: BackupScreenStateHolder) {
        when (event) {
            BackupScreenEvent.ExportDataEvent -> {
                _screenState.value = currentState.copy(isExporting = true)
                exportData()
            }
            BackupScreenEvent.ImportDataEvent -> {
                _screenState.value = currentState.copy(isImporting = true)
                importData()
            }
        }
    }

    private fun exportData() {
        Log.i("dujun", "export data")
    }

    private fun importData() {
        Log.i("dujun", "import data")
    }
}
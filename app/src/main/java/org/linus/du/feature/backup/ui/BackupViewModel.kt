package org.linus.du.feature.backup.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.linus.core.data.db.entities.Customer
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.backup.domain.repository.BackupRepository
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val repository: BackupRepository,
    private val customerRepository: CustomerRepository,
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
            BackupScreenEvent.ImportDataEventSuccess -> {
                _screenState.value = currentState.copy(isImporting = false)
            }
        }
    }

    private fun exportData() {
        Log.i("dujun", "export data")
    }

    private fun importData() {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 1 .. 11) {
                val customers = mutableListOf<Customer>()
                for (j in 1 .. 1000) {
                    customers.add(Customer(
                        id = "1380807975$j",
                        name = "super vip $j",
                        type = 3
                    ))
                }
                customerRepository.addCustomers(customers)
            }
            for (isec in 1 .. 11) {
                val customers = mutableListOf<Customer>()
                for (jo in 1 .. 1000) {
                    customers.add(Customer(
                        id = "1776128021$jo",
                        name = "normal vip $jo",
                        type = 2
                    ))
                }
                customerRepository.addCustomers(customers)
            }
            for (ithi in 1 .. 11) {
                val customers = mutableListOf<Customer>()
                for (k in 1 .. 1000) {
                    customers.add(Customer(
                        id = "1776128021$k",
                        name = "bad customer $k",
                        type = 1
                    ))
                }
                customerRepository.addCustomers(customers)
            }
            obtainEvent(BackupScreenEvent.ImportDataEventSuccess)
        }
    }
}
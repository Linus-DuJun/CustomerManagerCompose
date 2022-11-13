package org.linus.du.feature.backup.ui

import android.os.Environment
import android.os.Environment.DIRECTORY_DOCUMENTS
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.linus.core.data.db.entities.Customer
import org.linus.core.utils.extension.getHumanReadableDate
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.backup.domain.repository.BackupRepository
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val repository: BackupRepository,
    private val customerRepository: CustomerRepository,
    private val toaster: Toaster
): ViewModel() {

    private val _screenState = MutableStateFlow(BackupScreenStateHolder())
    val screenState = _screenState.asStateFlow()
    private val _storagePath = MutableLiveData<File>()


    fun setStoragePath(path: File) {
        _storagePath.value = path
    }

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
            BackupScreenEvent.ExportDataEventSuccess -> {
                _screenState.value = currentState.copy(isExporting = false)
            }
        }
    }

    private fun exportData() {
        viewModelScope.launch(Dispatchers.IO) {
            val customerFile: File = generateCustomerFile()
            val subjectFile: File = generateSubjectFile()
            val returnVisitFile: File = generateReturnVisitFile()

            Log.i("dujun", "file dir: ${customerFile.absoluteFile}")
            toaster.showToast("导出成功")
            obtainEvent(BackupScreenEvent.ExportDataEventSuccess)
        }
    }

    private fun importData() {
//        viewModelScope.launch(Dispatchers.IO) {
//            for (i in 1 .. 11) {
//                val customers = mutableListOf<Customer>()
//                for (j in 1 .. 1000) {
//                    customers.add(Customer(
//                        id = "1380807975$j",
//                        name = "super vip $j",
//                        type = 3
//                    ))
//                }
//                customerRepository.addCustomers(customers)
//            }
//            for (isec in 1 .. 11) {
//                val customers = mutableListOf<Customer>()
//                for (jo in 1 .. 1000) {
//                    customers.add(Customer(
//                        id = "1776128021$jo",
//                        name = "normal vip $jo",
//                        type = 2
//                    ))
//                }
//                customerRepository.addCustomers(customers)
//            }
//            for (ithi in 1 .. 11) {
//                val customers = mutableListOf<Customer>()
//                for (k in 1 .. 1000) {
//                    customers.add(Customer(
//                        id = "1776128021$k",
//                        name = "bad customer $k",
//                        type = 1
//                    ))
//                }
//                customerRepository.addCustomers(customers)
//            }
//            obtainEvent(BackupScreenEvent.ImportDataEventSuccess)
//        }
    }

    private fun generateCustomerFile(): File {
        return File(_storagePath.value, getCustomerCSVFileName())
    }

    private fun generateSubjectFile(): File {
        return File(_storagePath.value, getSubjectCSVFileName())
    }

    private fun generateReturnVisitFile(): File {
        return File(_storagePath.value, getReturnVisitCSVFileName())
    }


    private fun getCustomerCSVFileName(): String = "Customer_${getHumanReadableDate()}.csv"
    private fun getSubjectCSVFileName(): String = "Subject_${getHumanReadableDate()}.csv"
    private fun getReturnVisitCSVFileName() = "ReturnVisit_${getHumanReadableDate()}.csv"
}
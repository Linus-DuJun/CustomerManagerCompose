package org.linus.du.feature.backup.ui

import android.os.Environment
import android.os.Environment.DIRECTORY_DOCUMENTS
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.linus.core.data.db.entities.Customer
import org.linus.core.utils.extension.getHumanReadableDate
import org.linus.core.utils.extension.getReadableBirthday
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.backup.domain.repository.BackupRepository
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import java.io.File
import javax.inject.Inject
import kotlin.math.ceil

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
            val count = customerRepository.getCustomerCount()
            val times = (count / 300).toInt() + 1
            for (i in 0 .. times) {
                val customers = customerRepository.getCustomersByOffset(i)
                toaster.showToast("i is $i, customers size: ${customers.size}")
                csvWriter().open(customerFile, append = true) {
                    if (i == 0) {
                        writeRow(listOf("id", "name", "type", "birthday", "info"))
                    }
                    customers.forEachIndexed { index, customer ->
                        writeRow(listOf(customer.id, customer.name, customer.type, customer.birthday, customer.info))
                    }
                }
            }

            Log.i("dujun", "file dir: ${customerFile.absoluteFile}")
            toaster.showToast("导出成功")
            obtainEvent(BackupScreenEvent.ExportDataEventSuccess)
        }
    }

    private fun importData() {
        val customerFile = File(_storagePath.value, getCustomerCSVFileName())
        if (customerFile.exists()) {
            csvReader().open(customerFile) {
                readAllAsSequence().forEach { row: List<String> ->
//                    Log.i("dujun","id: ${row[0]}, name: ${row[1]}, type: ${row[2]}, birthday: ${row[3]}, info: ${row[4]}")
                    val customer = Customer(
                        id = row[0],
                        name = row[1],
                        type = row[2].toInt(),
                        birthday = row[3].toLong(),
                        info = row[4]
                    )
                    Log.i("dujun", "customer- id: ${customer.id}, name: ${customer.name}, type: ${customer.type}, birthday: ${getReadableBirthday(customer.birthday)}, info: ${customer.info}")
                }
            }
        }
//        val list = mutableListOf<Customer>()
//        for (i in 0 .. 1000) {
//            val customer = Customer(
//                id = "1380807$i",
//                name = "客户$i",
//                type =  i % 3,
//                birthday = System.currentTimeMillis(),
//                info = "$i 随便输入的客户信息，看看就好$i"
//            )
//            list.add(customer)
//        }
//        viewModelScope.launch(Dispatchers.IO) {
//            customerRepository.addCustomers(list)
//            obtainEvent(BackupScreenEvent.ImportDataEventSuccess)
//        }
    }

    private fun generateCustomerFile(): File {
        val file = File(_storagePath.value, getCustomerCSVFileName())
        if (file.exists()) file.delete()
        return File(_storagePath.value, getCustomerCSVFileName())
    }

    private fun generateSubjectFile(): File {
        return File(_storagePath.value, getSubjectCSVFileName())
    }

    private fun generateReturnVisitFile(): File {
        return File(_storagePath.value, getReturnVisitCSVFileName())
    }


    private fun getCustomerCSVFileName(): String = "Customer_Latest.csv"
    private fun getSubjectCSVFileName(): String = "Subject_Latest.csv"
    private fun getReturnVisitCSVFileName() = "ReturnVisit_Latest.csv"
}
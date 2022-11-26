package org.linus.du.feature.backup.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.linus.core.data.db.entities.Customer
import org.linus.core.data.db.entities.ReturnVisitEntity
import org.linus.core.data.db.entities.Subject
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.backup.domain.repository.BackupRepository
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import org.linus.du.feature.customer.domain.repository.RecordRepository
import org.linus.du.feature.customer.domain.repository.ReturnVisitRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val repository: BackupRepository,
    private val customerRepository: CustomerRepository,
    private val recordRepository: RecordRepository,
    private val returnVisitRepository: ReturnVisitRepository,
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
            BackupScreenEvent.ExportCustomerEvent -> {
                _screenState.value = currentState.copy(isExporting = true)
                exportCustomer()
            }
            BackupScreenEvent.ImportCustomerEvent -> {
                _screenState.value = currentState.copy(isImporting = true)
                importCustomer()
            }
            BackupScreenEvent.ImportCustomerSuccess -> {
                _screenState.value = currentState.copy(isImporting = false)
            }
            BackupScreenEvent.ExportCustomerSuccess -> {
                _screenState.value = currentState.copy(isExporting = false)
            }

            BackupScreenEvent.ExportRecordEvent -> {
                _screenState.value = currentState.copy(isExporting = true)
                exportRecord()
            }
            BackupScreenEvent.ImportRecordEvent -> {
                _screenState.value = currentState.copy(isImporting = true)
                importRecord()
            }
            BackupScreenEvent.ExportRecordSuccess -> {
                _screenState.value = currentState.copy(isExporting = false)
            }
            BackupScreenEvent.ImportRecordSuccess -> {
                _screenState.value = currentState.copy(isImporting = false)
            }

            BackupScreenEvent.ExportReturnVisitEvent -> {
                _screenState.value = currentState.copy(isExporting = true)
                exportReturnVisit()
            }
            BackupScreenEvent.ImportReturnVisitEvent -> {
                _screenState.value = currentState.copy(isImporting = true)
                importReturnVisit()
            }
            BackupScreenEvent.ExportReturnVisitSuccess -> {
                _screenState.value = currentState.copy(isExporting = false)
            }
            BackupScreenEvent.ImportReturnVisitSuccess -> {
                _screenState.value = currentState.copy(isImporting = false)
            }

        }
    }

    private fun exportRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            val subjectFile: File = generateSubjectFile()
            val count = recordRepository.getRecordCount()
            val times = (count / 300).toInt() + 1
            for (i in 0 .. times) {
                val subjects = recordRepository.getRecordsByOffset(i)
                csvWriter().open(subjectFile, append = true) {
                    if (i == 0) {
                        writeRow(listOf("id", "customer_name", "customer_phone", "subject", "time"))
                    }
                    subjects.forEach {
                        writeRow(listOf(it.id, it.customerName, it.customerPhone, it.subject, it.time))
                    }
                }
            }
            toaster.showToast("就诊表导出成功")
            obtainEvent(BackupScreenEvent.ExportRecordSuccess)
        }
    }

    private fun importRecord() {
        val recordFile = File(_storagePath.value, getSubjectCSVFileName())
        if (recordFile.exists()) {
            csvReader().open(recordFile) {
                readAllAsSequence().forEachIndexed { index, row: List<String> ->
                    if (index != 0) {
                        Subject(
                            id = row[0],
                            customerName = row[1],
                            customerPhone = row[2],
                            subject = row[3],
                            time = row[4].toLong()
                        ).let {
                            viewModelScope.launch(Dispatchers.IO) {
                                recordRepository.addRecord(it)
                            }
                        }
                    }
                }
            }
            toaster.showToast("就诊表导入成功")
            obtainEvent(BackupScreenEvent.ExportCustomerSuccess)
        } else {
            toaster.showToast("未找到备份文件")
            obtainEvent(BackupScreenEvent.ImportRecordSuccess)
        }
    }

    private fun exportReturnVisit() {
        viewModelScope.launch(Dispatchers.IO) {
            val returnVisitFile: File = generateReturnVisitFile()
            val count = returnVisitRepository.getReturnVisitCount()
            val times = (count / 200).toInt() + 1
            for (i in 0 .. times) {
                val returnVisits = returnVisitRepository.getReturnVisitByOffset(i)
                csvWriter().open(returnVisitFile, append = true) {
                    if (i == 0) {
                        writeRow(listOf("id", "customer_name", "customer_phone", "customer_type", "record_id", "record_title", "rv_title", "rv_time", "status"))
                    }
                    returnVisits.forEach {
                        writeRow(listOf(it.id, it.customerName, it.customerPhone, it.customerType, it.recordId, it.recordTitle, it.rvTitle, it.rvTime, it.status))
                    }
                }
            }
            toaster.showToast("回访记录导出成功")
            obtainEvent(BackupScreenEvent.ExportRecordSuccess)
        }
    }

    private fun importReturnVisit() {
        val returnVisitFile = File(_storagePath.value, getReturnVisitCSVFileName())
        if (returnVisitFile.exists()) {
            csvReader().open(returnVisitFile) {
                readAllAsSequence().forEachIndexed { index, row: List<String> ->
                    if (index != 0) {
                        ReturnVisitEntity(
                            id = row[0].toLong(),
                            customerName = row[1],
                            customerPhone = row[2],
                            customerType = row[3].toInt(),
                            recordId = row[4],
                            recordTitle = row[5],
                            rvTitle = row[6],
                            rvTime = row[7].toLong(),
                            status = row[8].toInt()
                        ).let {
                            viewModelScope.launch(Dispatchers.IO) {
                                returnVisitRepository.addReturnVisitItems(listOf(it))
                            }
                        }
                    }
                }
            }
            toaster.showToast("就诊表导入成功")
            obtainEvent(BackupScreenEvent.ExportCustomerSuccess)
        } else {
            toaster.showToast("未找到备份文件")
            obtainEvent(BackupScreenEvent.ImportRecordSuccess)
        }
    }

    private fun exportCustomer() {
        viewModelScope.launch(Dispatchers.IO) {
            val customerFile: File = generateCustomerFile()
            val count = customerRepository.getCustomerCount()
            val times = (count / 300).toInt() + 1
            for (i in 0 .. times) {
                val customers = customerRepository.getCustomersByOffset(i)
                csvWriter().open(customerFile, append = true) {
                    if (i == 0) {
                        writeRow(listOf("id", "name", "type", "birthday", "info"))
                    }
                    customers.forEachIndexed { index, customer ->
                        writeRow(listOf(customer.id, customer.name, customer.type, customer.birthday, customer.info))
                    }
                }
            }
            toaster.showToast("客户资料导出成功")
            obtainEvent(BackupScreenEvent.ExportCustomerSuccess)
        }
    }

    private fun importCustomer() {
        val customerFile = File(_storagePath.value, getCustomerCSVFileName())
        if (customerFile.exists()) {
            csvReader().open(customerFile) {
                readAllAsSequence().forEachIndexed { index: Int, row: List<String> ->
                    if (index != 0) {
                        Customer(
                            id = row[0],
                            name = row[1],
                            type = row[2].toInt(),
                            birthday = row[3].toLong(),
                            info = row[4]).let {
                            viewModelScope.launch(Dispatchers.IO) {
                                customerRepository.addCustomer(it)
                            }
                        }
                    }
                }
            }
            toaster.showToast("客户表导入成功")
            obtainEvent(BackupScreenEvent.ImportCustomerSuccess)
        } else {
            toaster.showToast("未找到备份文件")
            obtainEvent(BackupScreenEvent.ImportCustomerSuccess)
        }
    }

    private fun generateCustomerFile(): File {
        val file = File(_storagePath.value, getCustomerCSVFileName())
        if (file.exists()) file.delete()
        File(_storagePath.value, getCustomerCSVFileName()).apply {
            setReadable(true, false)
            setWritable(true, false)
            setExecutable(true, false)
        }.let {
            return it
        }
    }

    private fun generateSubjectFile(): File {
        val file = File(_storagePath.value, getSubjectCSVFileName())
        if (file.exists()) file.delete()
        File(_storagePath.value, getSubjectCSVFileName()).apply {
            setReadable(true, false)
            setWritable(true, false)
            setExecutable(true, false)
        }.let {
            return it
        }
    }

    private fun generateReturnVisitFile(): File {
        val file = File(_storagePath.value, getReturnVisitCSVFileName())
        if (file.exists()) file.delete()
        File(_storagePath.value, getReturnVisitCSVFileName()).apply {
            setReadable(true, false)
            setWritable(true, false)
            setExecutable(true, false)
        }.let {
            return it
        }
    }


    private fun getCustomerCSVFileName(): String = "Customer_Latest.csv"
    private fun getSubjectCSVFileName(): String = "Subject_Latest.csv"
    private fun getReturnVisitCSVFileName() = "ReturnVisit_Latest.csv"
}
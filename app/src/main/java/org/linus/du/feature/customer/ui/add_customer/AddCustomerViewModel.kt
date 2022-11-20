package org.linus.du.feature.customer.ui.add_customer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.linus.core.data.db.entities.Customer
import org.linus.core.data.db.entities.ReturnVisitEntity
import org.linus.core.data.db.entities.Subject
import org.linus.core.utils.extension.getHumanReadableDate
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import org.linus.du.feature.customer.domain.repository.RecordRepository
import org.linus.du.feature.customer.domain.repository.ReturnVisitRepository
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddCustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val recordRepository: RecordRepository,
    private val returnVisitRepository: ReturnVisitRepository,
    private val toaster: Toaster
) : ViewModel() {
    private val _screenState = MutableStateFlow(AddCustomerScreenStateHolder())
    val screenState = _screenState.asStateFlow()


    fun obtainEvent(event: AddCustomerScreenEvent) {
        reduce(event, _screenState.value)
    }

    private fun reduce(event: AddCustomerScreenEvent, currentState: AddCustomerScreenStateHolder) {
        when (event) {
            is AddCustomerScreenEvent.NameInputEvent ->
                _screenState.value = currentState.copy(name = event.name, noNameError = false)
            is AddCustomerScreenEvent.PhoneInputEvent ->
                _screenState.value = currentState.copy(phone = event.phone, noPhoneError = false)
            is AddCustomerScreenEvent.VipLevelSelectedEvent ->
                _screenState.value = currentState.copy(level = event.level, noLevelError = false)
            is AddCustomerScreenEvent.RecordSelectedEvent ->
                _screenState.value = currentState.copy(record = event.record, noRecordError = false)
            is AddCustomerScreenEvent.RecordInputEvent ->
                _screenState.value = currentState.copy(record = event.record, noRecordError = false)
            is AddCustomerScreenEvent.RecordDescInputEvent ->
                _screenState.value = currentState.copy(recordDescription = event.desc)
            is AddCustomerScreenEvent.NoNameErrorEvent ->
                _screenState.value = currentState.copy(noNameError = true)
            is AddCustomerScreenEvent.NoPhoneErrorEvent ->
                _screenState.value = currentState.copy(noPhoneError = true)
            is AddCustomerScreenEvent.NoLevelErrorEvent ->
                _screenState.value = currentState.copy(noLevelError = true)
            is AddCustomerScreenEvent.NoRecordErrorEvent ->
                _screenState.value = currentState.copy(noRecordError = true)
            is AddCustomerScreenEvent.OnAddReturnVisitButtonClickedEvent ->
                _screenState.value = currentState.copy(showAddReturnVisitDialog = true)
            is AddCustomerScreenEvent.OnAddReturnVisitCancelEvent ->
                _screenState.value = currentState.copy(showAddReturnVisitDialog = false)
            is AddCustomerScreenEvent.RecordDescInputEvent ->
                _screenState.value = currentState.copy(recordDescription = event.desc)
            is AddCustomerScreenEvent.OnReturnVisitTitleInputEvent -> {
                val returnVisit = ReturnVisit(
                    id = _screenState.value.currentAddingReturnVisit.id,
                    title = event.title,
                    timeStamp = _screenState.value.currentAddingReturnVisit.timeStamp,
                    humanReadableTime = _screenState.value.currentAddingReturnVisit.humanReadableTime
                )
                _screenState.value = currentState.copy(currentAddingReturnVisit = returnVisit, showAddReturnVisitDialog = true)
            }
            is AddCustomerScreenEvent.OnAddReturnVisitConfirmEvent -> {
                if (event.returnVisit.title.trim().isNullOrEmpty()) {
                    toaster.showToast("请输入回访简述")
                    return
                }
                if (event.returnVisit.timeStamp == 0L) {
                    toaster.showToast("请选择回访日期")
                    return
                }
                if (event.returnVisit.timeStamp <= System.currentTimeMillis()) {
                    toaster.showToast("回访日期不正确")
                    return
                }
                val newReturnVisitList = mutableListOf<ReturnVisit>()
                newReturnVisitList.addAll(_screenState.value.returnVisitItems)
                newReturnVisitList.add(event.returnVisit)
                _screenState.value = currentState.copy(
                    returnVisitItems = newReturnVisitList.toList(),
                    currentAddingReturnVisit = ReturnVisit(),
                    showAddReturnVisitDialog = false
                )
            }
            is AddCustomerScreenEvent.OnSelectDateEvent ->
                _screenState.value = currentState.copy(showAddReturnVisitDialog = false, showDatePickerDialog = true)
            is AddCustomerScreenEvent.OnReturnVisitDateConfirmedEvent -> {
                val returnVisit = ReturnVisit(
                    id = _screenState.value.currentAddingReturnVisit.id,
                    title = _screenState.value.currentAddingReturnVisit.title,
                    timeStamp = event.time,
                    humanReadableTime = event.humanReadableTime
                )
                _screenState.value = currentState.copy(
                    currentAddingReturnVisit = returnVisit,
                    showAddReturnVisitDialog = true,
                    showDatePickerDialog = false
                )
            }
            is AddCustomerScreenEvent.RemoveReturnVisitItemEvent -> {
                val deletedItem = _screenState.value.returnVisitItems.first { it.id == event.returnVisit.id }
                _screenState.value.returnVisitItems.minus(deletedItem).also {
                    _screenState.value = currentState.copy(returnVisitItems = it)
                }
            }
            is AddCustomerScreenEvent.SaveEvent -> {
                _screenState.value = currentState.copy(isLoading = true)
                saveCustomer()
            }
            is AddCustomerScreenEvent.FinishWithSuccessEvent -> {
                _screenState.value = currentState.copy(finishWithSuccess = true)
            }
            is AddCustomerScreenEvent.OnSelectBirthDayEvent -> {
                _screenState.value = currentState.copy(showBirthdayPickerDialog = true)
            }
            is AddCustomerScreenEvent.OnBirthdayConfirmedEvent -> {
                val newInfo = "生日: ${event.humanReadableTime}, ${currentState.customerInfo}"
                _screenState.value = currentState.copy(
                    showBirthdayPickerDialog = false,
                    humanReadableBirthDay = event.humanReadableTime,
                    birthDay = event.time,
                    customerInfo = newInfo
                )
            }
            is AddCustomerScreenEvent.OnSelectRecordDateEvent -> {
                _screenState.value = currentState.copy(showRecordDatePickerDialog = true)
            }
            is AddCustomerScreenEvent.OnRecordDateConfirmedEvent -> {
                val newRecord = "${event.humanReadableTime}, ${currentState.record}"
                _screenState.value = currentState.copy(
                    showRecordDatePickerDialog = false,
                    recordDate = event.time,
                    humanReadableRecordDate = event.humanReadableTime,
                    record = newRecord
                )
            }
            is AddCustomerScreenEvent.CustomerInfoInputEvent -> {
                _screenState.value = currentState.copy(
                    customerInfo = event.info
                )
            }
        }
    }

    private fun saveCustomer() {
        if (_screenState.value.name.isEmpty()) {
            toaster.showToast("请输入客户姓名")
            obtainEvent(AddCustomerScreenEvent.NoNameErrorEvent)
            return
        }
        if (_screenState.value.phone.isEmpty()) {
            toaster.showToast("请输入客户电话")
            obtainEvent(AddCustomerScreenEvent.NoPhoneErrorEvent)
            return
        }
        if (_screenState.value.level.isEmpty()) {
            toaster.showToast("请选择客户等级")
            obtainEvent(AddCustomerScreenEvent.NoLevelErrorEvent)
            return
        }
        if (_screenState.value.record.isEmpty()) {
            toaster.showToast("请选择客户诊疗项目")
            obtainEvent(AddCustomerScreenEvent.NoRecordErrorEvent)
            return
        }

        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.i("dujun", "${e.message}")
        }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val state = screenState.value
            val customer = Customer(
                id = state.phone,
                name = state.name,
                type = getCustomerType(state = state),
                birthday = state.birthDay,
                info = state.customerInfo
            )
            customerRepository.addCustomer(customer)
            val record = Subject(
                id = UUID.randomUUID().toString(),
                customerName = customer.name,
                customerPhone = customer.id,
                subject = state.record,
                time = System.currentTimeMillis()
            )
            recordRepository.addRecord(record)
            if (state.returnVisitItems.isNotEmpty()) {
                state.returnVisitItems.map {
                    ReturnVisitEntity(
                        id = it.id,
                        customerName = customer.name,
                        customerPhone = customer.id,
                        customerType = customer.type,
                        recordId = record.id,
                        recordTitle = record.subject,
                        rvTitle = it.title,
                        rvTime = it.timeStamp,
                        status =  1
                    )
                }.also {
                    returnVisitRepository.addReturnVisitItems(it)
                }
            }
            toaster.showToast("添加成功")
            obtainEvent(AddCustomerScreenEvent.FinishWithSuccessEvent)
        }
    }

    private fun getCustomerType(state: AddCustomerScreenStateHolder) =
        if (state.level ==  SUPER_VIP) 3 else if (state.level == NORMAL_VIP) 2 else 1


}
package org.linus.du.feature.customer.ui.add_customer

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import javax.inject.Inject

@HiltViewModel
class AddCustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
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
            is AddCustomerScreenEvent.SaveEvent -> saveCustomer()
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
        val state = _screenState.value
        Log.i("dujun", "name: ${state.name}, phone: ${state.phone}, level: ${state.level}, record: ${state.record}")
        if (state.returnVisitItems.isNotEmpty()) {
            state.returnVisitItems.forEachIndexed { index, returnVisit ->
                Log.i("dujun", "rv title: ${returnVisit.title}, ${returnVisit.humanReadableTime}")
            }
        }
    }
}
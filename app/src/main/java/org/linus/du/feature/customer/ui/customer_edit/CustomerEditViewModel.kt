package org.linus.du.feature.customer.ui.customer_edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.linus.core.data.db.entities.ReturnVisitEntity
import org.linus.core.data.db.entities.Subject
import org.linus.core.utils.extension.getHumanReadableDate
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import org.linus.du.feature.customer.domain.repository.RecordRepository
import org.linus.du.feature.customer.domain.repository.ReturnVisitRepository
import org.linus.du.feature.customer.ui.add_customer.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CustomerEditViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val recordRepository: RecordRepository,
    private val returnVisitRepository: ReturnVisitRepository,
    private val toaster: Toaster
) : ViewModel() {

    private val _screenState = MutableStateFlow(EditScreenStateHolder())
    val screenState = _screenState.asStateFlow()

    fun getCustomerInfo(id: String) {
        viewModelScope.launch (Dispatchers.IO) {
            customerRepository.getCustomer(id).collectLatest {
                obtainEvent(EditScreenEvent.CustomerLoadedEvent(it))
            }
        }
    }

    fun obtainEvent(event: EditScreenEvent) {
        reduce(event, _screenState.value)
    }

    private fun reduce(event: EditScreenEvent, currentState: EditScreenStateHolder) {
        when (event) {
            is EditScreenEvent.SaveEvent -> {
                _screenState.value = currentState.copy(isLoading = true)
                save()
            }
            is EditScreenEvent.CustomerLoadedEvent -> {
                _screenState.value = currentState.copy(
                    isLoading = false,
                    customer = event.customer,
                    customerInfo = event.customer.info
                )
            }
            is EditScreenEvent.VipLevelSelectedEvent -> {
                val customer = currentState.customer!!.copy(
                    type = getCustomerType(event.level)
                )
                _screenState.value = currentState.copy(customer = customer)
            }
            is EditScreenEvent.RecordInputEvent -> {
                _screenState.value = currentState.copy(record = event.record)
            }
            is EditScreenEvent.RecordSelectedEvent -> {
                _screenState.value = currentState.copy(record = event.record)
            }
            is EditScreenEvent.RecordDescInputEvent -> {
                _screenState.value = currentState.copy(recordDesc = event.desc)
            }
            is EditScreenEvent.OnAddReturnVisitButtonClickedEvent -> {
                _screenState.value = currentState.copy(showAddReturnVisitDialog = true)
            }
            is EditScreenEvent.OnAddReturnVisitCancelEvent -> {
                _screenState.value = currentState.copy(showAddReturnVisitDialog = false)
            }
            is EditScreenEvent.OnSelectDateEvent -> {
                _screenState.value = currentState.copy(showAddReturnVisitDialog = false, showDatePickerDialog = true)
            }
            is EditScreenEvent.OnReturnVisitTitleInputEvent -> {
                val returnVisit = ReturnVisit(
                    id = _screenState.value.currentAddingReturnVisit.id,
                    title = event.title,
                    timeStamp = _screenState.value.currentAddingReturnVisit.timeStamp,
                    humanReadableTime = _screenState.value.currentAddingReturnVisit.humanReadableTime
                )
                _screenState.value = currentState.copy(currentAddingReturnVisit = returnVisit, showAddReturnVisitDialog = true)
            }
            is EditScreenEvent.OnAddReturnVisitConfirmEvent -> {
                if (event.returnVisit.title.trim().isNullOrEmpty()) {
                    toaster.showToast("?????????????????????")
                    return
                }
                if (event.returnVisit.timeStamp == 0L) {
                    toaster.showToast("?????????????????????")
                    return
                }
                if (event.returnVisit.timeStamp <= System.currentTimeMillis()) {
                    toaster.showToast("?????????????????????")
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
            is EditScreenEvent.OnReturnVisitDateConfirmedEvent -> {
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
            is EditScreenEvent.RemoveReturnVisitItemEvent -> {
                val deletedItem = _screenState.value.returnVisitItems.first { it.id == event.returnVisit.id }
                _screenState.value.returnVisitItems.minus(deletedItem).also {
                    _screenState.value = currentState.copy(returnVisitItems = it)
                }
            }
            is EditScreenEvent.FinishWithSuccessEvent -> {
                _screenState.value = currentState.copy(finishWithSuccess = true)
            }
            is EditScreenEvent.OnSelectBirthDayEvent -> {
                _screenState.value = currentState.copy(showBirthdayPickerDialog = true)
            }
            is EditScreenEvent.OnSelectRecordDateEvent -> {
                _screenState.value = currentState.copy(showRecordDatePickerDialog = true)
            }
            is EditScreenEvent.BirthdayConfirmedEvent -> {
                val newInfo = "??????: ${event.humanReadableBirthday}, ${currentState.customerInfo}"
                val customer = currentState.customer!!.copy(
                    birthday = event.time
                )
                _screenState.value = currentState.copy(
                    showBirthdayPickerDialog = false,
                    humanReadableBirthday = event.humanReadableBirthday,
                    birthday = event.time,
                    customerInfo = newInfo,
                    customer = customer
                )
            }
            is EditScreenEvent.RecordDateConfirmedEvent -> {
                val newRecord = "${event.humanReadableDate}, ${currentState.record}"
                _screenState.value = currentState.copy(
                    showRecordDatePickerDialog = false,
                    recordDate = event.time,
                    humanReadableRecordDate = event.humanReadableDate,
                    record = newRecord
                )
            }
            is EditScreenEvent.CustomerInfoInputEvent -> {
                val customer = currentState.customer!!.copy(
                    info = event.info
                )
                _screenState.value = currentState.copy(
                    customerInfo = event.info,
                    customer = customer
                )
            }
        }
    }

    private fun getCustomerType(state: String) =
        if (state ==  SUPER_VIP) 3 else if (state == NORMAL_VIP) 2 else 1

    private fun save() {
        if (_screenState.value.record.isNotEmpty() &&
                _screenState.value.recordDate == 0L) {
            toaster.showToast("?????????????????????")
            return
        }

        val exceptionHandler = CoroutineExceptionHandler { _, e ->
            Log.i("dujun", "${e.message}")
        }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val state = screenState.value
            customerRepository.addCustomer(state.customer!!)
            if (state.record.isNotEmpty()) {
                val record = Subject(
                    id = UUID.randomUUID().toString(),
                    customerName = state.customer.name,
                    customerPhone = state.customer.id,
                    subject = state.record,
                    time = System.currentTimeMillis()
                )
                recordRepository.addRecord(record)
                if (state.returnVisitItems.isNotEmpty()) {
                    state.returnVisitItems.map {
                        ReturnVisitEntity(
                            id = it.id,
                            customerName = state.customer.name,
                            customerPhone = state.customer.id,
                            customerType = state.customer.type,
                            recordId = record.id,
                            recordTitle = record.subject,
                            rvTitle = it.title,
                            rvTime = it.timeStamp,
                            status = 1
                        )
                    }.also {
                        returnVisitRepository.addReturnVisitItems(it)
                    }
                }
            }
            toaster.showToast("?????????????????????")
            obtainEvent(EditScreenEvent.FinishWithSuccessEvent)
        }
    }
}
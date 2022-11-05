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
                    title = event.title,
                    timeStamp = _screenState.value.currentAddingReturnVisit.timeStamp,
                    humanReadableTime = _screenState.value.currentAddingReturnVisit.humanReadableTime
                )
                _screenState.value = currentState.copy(currentAddingReturnVisit = returnVisit, showAddReturnVisitDialog = true)
            }
            is AddCustomerScreenEvent.OnAddReturnVisitConfirmEvent -> {
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
                val deletedItem = _screenState.value.returnVisitItems.first { it.title == event.returnVisit.title }
                _screenState.value.returnVisitItems.minus(deletedItem).also {
                    _screenState.value = currentState.copy(returnVisitItems = it)
                }
            }
            is AddCustomerScreenEvent.SaveEvent -> saveCustomer()
        }
    }

    private fun saveCustomer() {

    }
}
package org.linus.du.feature.customer.ui.add_customer

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
            is AddCustomerScreenEvent.SaveEvent -> saveCustomer()
        }
    }

    private fun saveCustomer() {
        toaster.showToast("${screenState.value.name}, ${screenState.value.phone}, ${screenState.value.level}, ${screenState.value.record}")
    }
}
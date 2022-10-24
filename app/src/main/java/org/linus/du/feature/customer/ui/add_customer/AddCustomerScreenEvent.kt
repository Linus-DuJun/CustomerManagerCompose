package org.linus.du.feature.customer.ui.add_customer

sealed class AddCustomerScreenEvent {
    object SaveEvent: AddCustomerScreenEvent()
    object NoNameErrorEvent: AddCustomerScreenEvent()
    object NoPhoneErrorEvent: AddCustomerScreenEvent()
    object NoLevelErrorEvent: AddCustomerScreenEvent()
    data class RecordSelectedEvent(val record: String): AddCustomerScreenEvent()
    data class CustomerLevelSelectedEvent(val level: String): AddCustomerScreenEvent()
    data class NameInputEvent(val name: String): AddCustomerScreenEvent()
    data class PhoneInputEvent(val phone: String): AddCustomerScreenEvent()
    data class RecordDescInputEvent(val desc: String): AddCustomerScreenEvent()
}
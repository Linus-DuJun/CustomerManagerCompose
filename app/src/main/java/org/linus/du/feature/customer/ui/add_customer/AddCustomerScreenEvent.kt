package org.linus.du.feature.customer.ui.add_customer

sealed class AddCustomerScreenEvent {
    object SaveEvent: AddCustomerScreenEvent()
    object NoNameErrorEvent: AddCustomerScreenEvent()
    object NoPhoneErrorEvent: AddCustomerScreenEvent()
    object NoLevelErrorEvent: AddCustomerScreenEvent()
    object NoRecordErrorEvent: AddCustomerScreenEvent()
    object OnAddReturnVisitButtonClickedEvent: AddCustomerScreenEvent()
    object OnAddReturnVisitCancelEvent: AddCustomerScreenEvent()
    object OnSelectDateEvent: AddCustomerScreenEvent()
    data class RecordDescriptionInputEvent(val desc: String) : AddCustomerScreenEvent()
    data class RemoveReturnVisitItemEvent(val returnVisit: ReturnVisit) : AddCustomerScreenEvent()
    data class OnReturnVisitDateConfirmedEvent(val time: Long, val humanReadableTime: String): AddCustomerScreenEvent()
    data class OnAddReturnVisitConfirmEvent(val returnVisit: ReturnVisit): AddCustomerScreenEvent()
    data class OnReturnVisitTitleInputEvent(val title: String): AddCustomerScreenEvent()
    data class RecordInputEvent(val record: String): AddCustomerScreenEvent()
    data class RecordSelectedEvent(val record: String): AddCustomerScreenEvent()
    data class VipLevelSelectedEvent(val level: String): AddCustomerScreenEvent()
    data class NameInputEvent(val name: String): AddCustomerScreenEvent()
    data class PhoneInputEvent(val phone: String): AddCustomerScreenEvent()
    data class RecordDescInputEvent(val desc: String): AddCustomerScreenEvent()

}
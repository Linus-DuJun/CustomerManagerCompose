package org.linus.du.feature.customer.ui.customer_edit

import org.linus.core.data.db.entities.Customer
import org.linus.du.feature.customer.ui.add_customer.AddCustomerScreenEvent
import org.linus.du.feature.customer.ui.add_customer.ReturnVisit

sealed class EditScreenEvent {
    object SaveEvent: EditScreenEvent()
    object FinishWithSuccessEvent: EditScreenEvent()
    object OnAddReturnVisitButtonClickedEvent: EditScreenEvent()
    object OnAddReturnVisitCancelEvent: EditScreenEvent()
    object OnSelectDateEvent: EditScreenEvent()
    object OnSelectBirthDayEvent: EditScreenEvent()
    object OnSelectRecordDateEvent: EditScreenEvent()
    data class BirthdayConfirmedEvent(val time: Long, val humanReadableBirthday: String): EditScreenEvent()
    data class RecordDateConfirmedEvent(val time: Long, val humanReadableDate: String) : EditScreenEvent()
    data class CustomerLoadedEvent(val customer: Customer) : EditScreenEvent()
    data class RecordInputEvent(val record: String): EditScreenEvent()
    data class RecordSelectedEvent(val record: String): EditScreenEvent()
    data class VipLevelSelectedEvent(val level: String): EditScreenEvent()
    data class RecordDescInputEvent(val desc: String): EditScreenEvent()
    data class OnReturnVisitTitleInputEvent(val title: String): EditScreenEvent()
    data class OnAddReturnVisitConfirmEvent(val returnVisit: ReturnVisit): EditScreenEvent()
    data class RemoveReturnVisitItemEvent(val returnVisit: ReturnVisit) : EditScreenEvent()
    data class OnReturnVisitDateConfirmedEvent(val time: Long, val humanReadableTime: String): EditScreenEvent()
    data class CustomerInfoInputEvent(val info: String): EditScreenEvent()

}
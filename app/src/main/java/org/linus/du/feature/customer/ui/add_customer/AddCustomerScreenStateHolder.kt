package org.linus.du.feature.customer.ui.add_customer

import androidx.compose.runtime.Stable
import org.linus.core.utils.extension.empty

@Stable
data class ReturnVisit(
    val id: Long = System.currentTimeMillis(),
    val title: String = String.empty(),
    val timeStamp: Long = 0L,
    val humanReadableTime: String = String.empty()
)

data class AddCustomerScreenStateHolder (
    val isLoading: Boolean = false,
    val finishWithSuccess: Boolean = false,
    val showAddReturnVisitDialog: Boolean = false,
    val showDatePickerDialog: Boolean = false,
    val showBirthdayPickerDialog: Boolean = false,
    val showRecordDatePickerDialog: Boolean = false,
    val humanReadableBirthDay: String = String.empty(),
    val birthDay: Long = 0L,
    val humanReadableRecordDate: String = String.empty(),
    val recordDate: Long = 0L,
    val name: String = String.empty(),
    val phone: String = String.empty(),
    val level: String = String.empty(),
    val record: String = String.empty(),
    val customerInfo: String = String.empty(),
    val recordDescription: String = String.empty(),
    val currentAddingReturnVisit: ReturnVisit = ReturnVisit(),
    val returnVisitItems: List<ReturnVisit> = listOf(),
    val noNameError: Boolean = false,
    val noPhoneError: Boolean = false,
    val noLevelError: Boolean = false,
    val noRecordError: Boolean = false
)
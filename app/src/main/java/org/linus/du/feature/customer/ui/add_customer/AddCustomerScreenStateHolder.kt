package org.linus.du.feature.customer.ui.add_customer

import androidx.compose.runtime.Stable
import org.linus.core.utils.extension.empty

@Stable
data class ReturnVisit(
    val title: String = String.empty(),
    val timeStamp: Long = 0L,
    val humanReadableTime: String = String.empty()
)

data class AddCustomerScreenStateHolder (
    val isLoading: Boolean = true,
    val showAddReturnVisitDialog: Boolean = false,
    val showDatePickerDialog: Boolean = false,
    val name: String = String.empty(),
    val phone: String = String.empty(),
    val level: String = String.empty(),
    val record: String = String.empty(),
    val recordDescription: String = String.empty(),
    val currentAddingReturnVisit: ReturnVisit = ReturnVisit(),
    val returnVisitItems: List<ReturnVisit> = listOf(),
    val noNameError: Boolean = false,
    val noPhoneError: Boolean = false,
    val noLevelError: Boolean = false,
    val noRecordError: Boolean = false
)
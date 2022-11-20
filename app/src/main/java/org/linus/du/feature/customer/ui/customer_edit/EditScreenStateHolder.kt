package org.linus.du.feature.customer.ui.customer_edit

import org.linus.core.data.db.entities.Customer
import org.linus.core.utils.extension.empty
import org.linus.du.feature.customer.ui.add_customer.ReturnVisit

data class EditScreenStateHolder(
    val isLoading: Boolean = true,
    val finishWithSuccess: Boolean = false,
    val showAddReturnVisitDialog: Boolean = false,
    val showDatePickerDialog: Boolean = false,
    val showBirthdayPickerDialog: Boolean = false,
    val showRecordDatePickerDialog: Boolean = false,
    val record: String = String.empty(),
    val recordDesc: String = String.empty(),
    val currentAddingReturnVisit: ReturnVisit = ReturnVisit(),
    val returnVisitItems: List<ReturnVisit> = listOf(),
    val customerInfo: String = String.empty(),
    val humanReadableBirthday: String = String.empty(),
    val birthday: Long = 0L,
    val recordDate: Long = 0L,
    val humanReadableRecordDate: String = String.empty(),
    val customer: Customer? = null,
)
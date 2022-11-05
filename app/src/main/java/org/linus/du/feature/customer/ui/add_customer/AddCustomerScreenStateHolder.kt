package org.linus.du.feature.customer.ui.add_customer

import org.linus.core.utils.extension.empty

data class AddCustomerScreenStateHolder (
    val isLoading: Boolean = true,
    val name: String = String.empty(),
    val phone: String = String.empty(),
    val level: String = String.empty(),
    val record: String = String.empty(),
    val recordDescription: String = String.empty(),
    val noNameError: Boolean = false,
    val noPhoneError: Boolean = false,
    val noLevelError: Boolean = false,
    val noRecordError: Boolean = false
)
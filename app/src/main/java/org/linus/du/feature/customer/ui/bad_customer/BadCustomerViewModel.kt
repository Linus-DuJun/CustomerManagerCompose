package org.linus.du.feature.customer.ui.bad_customer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import javax.inject.Inject

@HiltViewModel
class BadCustomerViewModel @Inject constructor(
    private val repository: CustomerRepository,
    private val toaster: Toaster
) : ViewModel() {
    val numbers = buildList<Int> {
        for (i in 0 .. 50) {
            this.add(i)
        }
    }
}
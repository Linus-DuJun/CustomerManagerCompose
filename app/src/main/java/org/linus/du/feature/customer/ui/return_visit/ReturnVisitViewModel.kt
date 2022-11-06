package org.linus.du.feature.customer.ui.return_visit

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.customer.domain.repository.ReturnVisitRepository
import javax.inject.Inject

@HiltViewModel
class ReturnVisitViewModel @Inject constructor(
    private val repository: ReturnVisitRepository,
    toaster: Toaster
) : ViewModel() {
    val numbers = buildList<Int> {
        for (i in 0 .. 50) {
            this.add(i)
        }
    }
    val returnVisit = numbers.map { "Item $it" }
}
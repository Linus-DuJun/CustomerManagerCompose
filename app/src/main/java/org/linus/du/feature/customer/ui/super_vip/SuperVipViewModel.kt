package org.linus.du.feature.customer.ui.super_vip

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import javax.inject.Inject

@HiltViewModel
class SuperVipViewModel @Inject constructor(
    private val repository: CustomerRepository,
    private val toaster: Toaster
) : ViewModel() {

    private val _screenState = MutableStateFlow(SuperVipScreenStateHolder())
    private val screenState = _screenState.asStateFlow()

    val numbers = buildList<Int> {
        for (i in 0 .. 50) {
            this.add(i)
        }
    }
}
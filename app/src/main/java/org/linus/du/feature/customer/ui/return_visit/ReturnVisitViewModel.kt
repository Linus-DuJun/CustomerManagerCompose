package org.linus.du.feature.customer.ui.return_visit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.customer.domain.repository.ReturnVisitRepository
import javax.inject.Inject

@HiltViewModel
class ReturnVisitViewModel @Inject constructor(
    private val repository: ReturnVisitRepository,
    toaster: Toaster
) : ViewModel() {

    private val _screenState = MutableStateFlow(ReturnVisitScreenStateHolder())
    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getReturnVisitItems().collectLatest {
                obtainEvent(ReturnVisitScreenEvent.ItemsLoadedEvent(items = it))
            }
        }
    }

    fun obtainEvent(event: ReturnVisitScreenEvent) {
        reduce(event, _screenState.value)
    }

    private fun reduce(event: ReturnVisitScreenEvent, state: ReturnVisitScreenStateHolder) {
        when(event) {
            is ReturnVisitScreenEvent.ItemsLoadedEvent -> {
                _screenState.value = state.copy(isLoading = false, items = event.items)
            }
        }
    }
}
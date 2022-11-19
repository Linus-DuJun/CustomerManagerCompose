package org.linus.du.feature.customer.ui.search

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(SearchScreenStateHolder())
    val screenState = _screenState.asStateFlow()

    fun obtainEvent(event: SearchScreenEvent) {
        reduce(event, screenState.value)
    }

    private fun reduce(event: SearchScreenEvent, currentState: SearchScreenStateHolder) {
        when (event) {
            is SearchScreenEvent.SearchKeyInputEvent -> {
                _screenState.value = currentState.copy(keyword = event.keyword, isError = event.keyword.trim().isEmpty())
            }
            is SearchScreenEvent.SearchConfirmEvent -> {
                if (_screenState.value.keyword.isEmpty()) {
                    obtainEvent(SearchScreenEvent.NoSearchKeyWordEvent)
                    return
                }
                search()
            }
            is SearchScreenEvent.NoSearchKeyWordEvent -> {
                _screenState.value = currentState.copy(isError = true)
            }
            is SearchScreenEvent.SearchResultEvent -> {
                _screenState.value = currentState.copy(result = event.customers)
            }
        }
    }

    private fun search() {
        viewModelScope.launch(Dispatchers.IO) {
            customerRepository.getCustomerByName(screenState.value.keyword.trim()).collectLatest {
                obtainEvent(SearchScreenEvent.SearchResultEvent(it))
            }
        }
    }
}



//IconButton(onClick = { expanded = !expanded }) {
//    Icon(
//        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
//        contentDescription = if (expanded) {
//            stringResource(R.string.show_less)
//        } else {
//            stringResource(R.string.show_more)
//        }
//
//    )
//}
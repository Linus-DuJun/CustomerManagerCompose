package org.linus.du.feature.customer.ui.custom_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.linus.core.data.db.entities.Customer
import org.linus.core.data.db.entities.Subject
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import org.linus.du.feature.customer.domain.repository.RecordRepository
import javax.inject.Inject

@HiltViewModel
class CustomerInfoViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val recordRepository: RecordRepository,
) : ViewModel() {

    private val _customer = MutableStateFlow<Customer?>(null)
    val customer = _customer.asStateFlow()
    private val _records = MutableStateFlow<List<Subject>>(listOf())
    val records = _records.asStateFlow()

    fun getCustomerInfo(id: String) {
        viewModelScope.launch (Dispatchers.IO) {
            customerRepository.getCustomer(id).collectLatest {
                _customer.value = it
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            recordRepository.getRecordByCustomer(id).collectLatest {
                _records.value = it
            }
        }
    }
}
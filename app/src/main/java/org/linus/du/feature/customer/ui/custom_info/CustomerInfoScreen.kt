package org.linus.du.feature.customer.ui.custom_info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.linus.core.data.db.entities.Customer
import org.linus.core.data.db.entities.Subject
import org.linus.core.ui.common.AnimatedLoadingView
import org.linus.core.ui.common.BaseTopAppBar
import org.linus.core.utils.extension.getReadableDateByTime


@Composable
fun CustomerInfoScreen(
    viewModel: CustomerInfoViewModel,
    onBackClick: () -> Unit
) {
    val customer = viewModel.customer.collectAsState()
    val records = viewModel.records.collectAsState()
    Scaffold(
        topBar = { BaseTopAppBar(
            title = "${customer.value?.name} ${getLevelString(customer.value?.type)} ${customer.value?.id}",
            onBackClick = onBackClick
        ) }
    ) {
        Column {
            Text("客户信息:", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(16.dp))
            if (customer.value != null) {
                Text(
                    customer.value!!.info,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "诊疗记录:", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(16.dp))
                if (records.value.isNotEmpty()) {
                    RecordListView(records = records.value)
                }
            } else {
                AnimatedLoadingView(visible = true)
            }
        }
    }
}

@Composable
private fun CustomerInfoView(customer: Customer) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = customer.name, style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = getLevelString(customer.type), style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = customer.id, style = MaterialTheme.typography.body1)
    }
}

private fun getLevelString(type: Int?) = when (type) {
    1 -> "客诉"
    2 -> "普通"
    3-> "超V"
    else -> ""
}

@Composable
private fun RecordListView(records: List<Subject>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        itemsIndexed(records) { index, item ->
            RecordItemView(index = index, record = item)
        }
    }
}

@Composable
private fun RecordItemView(index: Int, record: Subject) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${index + 1}", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = record.subject, style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = getReadableDateByTime(record.time), style = MaterialTheme.typography.body1)
            }
            Spacer(modifier = Modifier.height(4.dp))
//            Text(record.subject, style = MaterialTheme.typography.body1)
        }
    }
}


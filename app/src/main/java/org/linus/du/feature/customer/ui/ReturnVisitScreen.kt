package org.linus.du.feature.customer.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import org.linus.core.data.db.entities.ReturnVisitEntity
import org.linus.core.ui.common.AddCustomerButton
import org.linus.core.ui.common.LoadingView
import org.linus.core.ui.common.SearchButton
import org.linus.core.ui.theme.*
import org.linus.core.utils.extension.getReadableDateByTime
import org.linus.du.R
import org.linus.du.feature.customer.ui.return_visit.ReturnVisitViewModel
import org.linus.du.feature.customer.ui.search.SearchActivity

@Composable
fun ReturnVisitScreen(
    viewModel: ReturnVisitViewModel = hiltViewModel(),
    refresh: () -> Unit,
    onAddCustomer: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ReturnVisitAppBar(
                onAddCustomer = onAddCustomer,
                refreshing = false,
                onRefreshActionClick = refresh
            )
        },
        modifier = Modifier
    ) { paddingValues ->
        val screenState = viewModel.screenState.collectAsState()
        if (screenState.value.isLoading) {
            LoadingView()
        } else {
            if (screenState.value.items.isEmpty()) {
                EmptyView()
            } else {
                ReturnVisitListView(
                    items = screenState.value.items,
                    delete = { viewModel.deleteReturnVisit(it) }
                )
            }
        }
    }
}

@Composable
private fun ReturnVisitListView(
    items: List<ReturnVisitEntity>,
    delete: (ReturnVisitEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(vertical = 56.dp)
    ) {
        items(items = items) { item ->
            ReturnVisitItemView(item = item, delete = delete)
            Divider(color = Gray200)
        }
    }
}

@Composable
private fun EmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = R.string.string_empty),
            style = MaterialTheme.typography.h5,
        )
    }
}

@Composable
private fun ReturnVisitItemView(
    item: ReturnVisitEntity,
    delete: (ReturnVisitEntity) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_date_picker),
            contentDescription = null,
            tint = if (item.customerType == 1) Red500 else Green,
            modifier = Modifier.size(width = 22.dp, height = 24.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Text(item.customerName, style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.width(8.dp))
                Text(item.customerPhone, style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.width(8.dp))
                Text(getReadableDateByTime(item.rvTime), style = MaterialTheme.typography.body1)
            }
            Text("${item.rvTitle}", style = MaterialTheme.typography.body2)
        }
        IconButton(onClick = { delete.invoke(item) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ReturnVisitAppBar(
    onAddCustomer: () -> Unit,
    refreshing: Boolean,
    onRefreshActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background.copy(
            alpha = 0.97f
        ),
        contentColor = MaterialTheme.colors.onSurface,
        contentPadding = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top).asPaddingValues(),
        modifier = modifier,
        title = { Text(text = stringResource(id = R.string.return_visit_schedule)) },
        actions = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
//                Crossfade(
//                    targetState = refreshing,
//                    modifier = Modifier.align(Alignment.CenterVertically)
//                ) { isRefreshing ->
//                    if (!isRefreshing) {
//                        RefreshButton(onClick = onRefreshActionClick)
//                    }
//                }
                SearchButton(onClick = {
                    Intent(context, SearchActivity::class.java).let {
                        context.startActivity(it)
                    }
                })
                AddCustomerButton(onClick = onAddCustomer)
            }
        }
    )
}
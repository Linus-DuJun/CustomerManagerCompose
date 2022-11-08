package org.linus.du.feature.customer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.linus.core.data.db.entities.ReturnVisitEntity
import org.linus.core.ui.common.AddCustomerButton
import org.linus.core.ui.common.LoadingView
import org.linus.du.R
import org.linus.du.feature.customer.ui.return_visit.ReturnVisitViewModel

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
                    items = screenState.value.items
                )
            }
        }
    }
}

@Composable
private fun ReturnVisitListView(
    items: List<ReturnVisitEntity>,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 72.dp)
    ) {
        items(items = items) { item ->
            ReturnVisitItemView(item = item)
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
private fun ReturnVisitItemView(item: ReturnVisitEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(item.rvTitle)
    }
}

@Composable
private fun ReturnVisitAppBar(
    onAddCustomer: () -> Unit,
    refreshing: Boolean,
    onRefreshActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                AddCustomerButton(onClick = onAddCustomer)
            }
        }
    )
}
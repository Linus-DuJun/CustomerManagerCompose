package org.linus.du.feature.customer.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import org.linus.core.ui.common.AddCustomerButton
import org.linus.core.ui.common.RefreshButton
import org.linus.core.utils.extension.Layout
import org.linus.core.utils.extension.bodyWidth
import org.linus.du.R
import org.linus.du.feature.customer.ui.add_customer.ReturnVisit
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
                onRefreshActionClick = refresh)
        },
        modifier = Modifier
    ) { paddingValues ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = false),
            onRefresh = refresh,
            indicatorPadding = paddingValues,
            indicator = { state, trigger ->
                SwipeRefreshIndicator(state = state, refreshTriggerDistance = trigger, scale = true)
            }
        ) {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.bodyWidth()
            ) {
                item {
                    Spacer(modifier = Modifier.height(Layout.gutter))
                }
                item {
                    Text("新回访页面")
                }
                items(viewModel.returnVisit) { item ->
                    ReturnVisitItemView(item = item)
                }
            }
        }
    }
}

@Composable
private fun ReturnVisitItemView(item: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(item)
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
                Crossfade(
                    targetState = refreshing,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) { isRefreshing ->
                    if (!isRefreshing) {
                        RefreshButton(onClick = onRefreshActionClick)
                    }
                }
                AddCustomerButton(onClick = onAddCustomer)
            }
        }
    )
}
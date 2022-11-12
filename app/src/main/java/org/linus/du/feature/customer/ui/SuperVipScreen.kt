package org.linus.du.feature.customer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import org.linus.core.data.db.entities.Customer
import org.linus.core.ui.common.AddCustomerButton
import org.linus.core.ui.common.ErrorItem
import org.linus.core.ui.common.LoadingView
import org.linus.core.ui.theme.Gray300
import org.linus.core.ui.theme.Green
import org.linus.core.utils.extension.bodyWidth
import org.linus.du.R
import org.linus.du.feature.customer.ui.super_vip.SuperVipViewModel

@Composable
fun SuperVipScreen(
    viewModel: SuperVipViewModel = hiltViewModel(),
    refresh: () -> Unit,
    onCheckCustomerDetailInfo: (Customer) -> Unit,
    onAddCustomer: () -> Unit,
    onShowBottomSheet: (Customer) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SuperVipAppBar(
                onAddCustomer = onAddCustomer,
                refreshing = false,
                onRefreshActionClick = refresh
            )
        },
        modifier = Modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 56.dp)
        ) {
            SuperVipContentView(
                viewModel = viewModel,
                onCheckCustomerDetailInfo = onCheckCustomerDetailInfo,
                onShowBottomSheet = onShowBottomSheet
            )
        }
    }
}

@Composable
private fun SuperVipContentView(
    viewModel: SuperVipViewModel,
    onCheckCustomerDetailInfo: (Customer) -> Unit,
    onShowBottomSheet: (Customer) -> Unit,
    modifier: Modifier = Modifier
) {
    val superCustomers = viewModel.superCustomers.collectAsLazyPagingItems()
    LazyColumn(
        modifier = Modifier
            .bodyWidth()
            .padding(start = 16.dp)
    ) {
        items(items = superCustomers) { customer ->
            SuperViewItemView(
                customer = customer!!,
                onCheckCustomerDetailInfo = onCheckCustomerDetailInfo,
                onShowBottomSheet = onShowBottomSheet
            )
        }
        superCustomers.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView() }
                }
                loadState.refresh is LoadState.NotLoading -> {
                    // TODO add empty
                }
                loadState.refresh is LoadState.Error -> {
                    val e = superCustomers.loadState.refresh as LoadState.Error
                    // TODO add error item and retry
                }
                loadState.append is LoadState.Error -> {
                    val e = superCustomers.loadState.append as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            onClickRetry = { retry() }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun SuperViewItemView(
    customer: Customer,
    onCheckCustomerDetailInfo: (Customer) -> Unit,
    onShowBottomSheet: (Customer) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                onCheckCustomerDetailInfo.invoke(customer)
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_bookmark),
            contentDescription = null,
            tint = Green,
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
                Text(customer.name, style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.width(8.dp))
//                Text("32岁", style = MaterialTheme.typography.body1)
            }
            Text(customer.id, style = MaterialTheme.typography.body2)
        }
        Spacer(modifier = Modifier.padding(8.dp))
        IconButton(
            onClick = { onShowBottomSheet(customer) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = null,
                tint = Gray300
            )
        }
    }
}

@Composable
private fun SuperVipAppBar(
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
        title = { Text(text = stringResource(id = R.string.super_vip_title)) },
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
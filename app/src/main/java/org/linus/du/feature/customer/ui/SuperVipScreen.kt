package org.linus.du.feature.customer.ui

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.linus.core.data.db.entities.Customer
import org.linus.core.ui.common.AddCustomerButton
import org.linus.core.ui.common.BackupButton
import org.linus.core.ui.common.RefreshButton
import org.linus.core.ui.theme.Gray300
import org.linus.core.ui.theme.Green
import org.linus.core.utils.extension.Layout
import org.linus.core.utils.extension.bodyWidth
import org.linus.du.R
import org.linus.du.feature.customer.ui.super_vip.SuperVipViewModel

@Composable
fun SuperVipScreen(
    viewModel: SuperVipViewModel = hiltViewModel(),
    refresh: () -> Unit,
    onCheckCustomerDetailInfo: () -> Unit,
    onAddCustomer: () -> Unit,
    onBackup: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SuperVipAppBar(
                onAddCustomer = onAddCustomer,
                refreshing = false,
                onRefreshActionClick = refresh,
                onBackup = onBackup
            )
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
            SuperVipContentView(
                viewModel = viewModel,
                onCheckCustomerDetailInfo = onCheckCustomerDetailInfo
            )
        }
    }
}

@Composable
private fun SuperVipContentView(
    viewModel: SuperVipViewModel,
    onCheckCustomerDetailInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier
            .bodyWidth()
    ) {
        items(viewModel.numbers) { num ->
            SuperViewItemView(sv = "Super VIP $num", onCheckCustomerDetailInfo = onCheckCustomerDetailInfo)
        }
    }
}

@Composable
private fun SuperViewItemView(
    onCheckCustomerDetailInfo: () -> Unit,
    sv: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                onCheckCustomerDetailInfo.invoke()
                Log.i("dujun", "check customer info")
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
                Text(sv, style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.width(8.dp))
                Text("32岁", style = MaterialTheme.typography.body1)
            }
            Text("1776280217", style = MaterialTheme.typography.body2)
        }
        Spacer(modifier = Modifier.padding(8.dp))
        IconButton(
            onClick = { Log.i("dujun", "show bottom sheet") }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
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
    onBackup: () -> Unit,
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
                BackupButton(onClick = onBackup)
                AddCustomerButton(onClick = onAddCustomer)
            }
        }
    )
}
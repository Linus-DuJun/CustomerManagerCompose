package org.linus.du

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.google.accompanist.insets.ui.BottomNavigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import kotlinx.coroutines.launch
import org.linus.core.data.db.entities.Customer
import org.linus.core.ui.theme.Ocean300

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    onAddCustomer: () -> Unit,
    onEditCustomer: (Customer) -> Unit,
    onCheckCustomerInfo: (Customer) -> Unit,
    onBackup: () -> Unit,
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberAnimatedNavController(bottomSheetNavigator)

    Scaffold(
        bottomBar = {
            val currentSelectedItem by navController.currentScreenAsState()
            HomeBottomNavigation(
                selectedNavigation = currentSelectedItem,
                onNavigationSelected = { selected ->
                    navController.navigate(selected.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth())
        },
        drawerContent = { DrawerView(onBackup = onBackup) }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            com.google.accompanist.navigation.material.ModalBottomSheetLayout(bottomSheetNavigator) {
                AppNavigation(
                    navController = navController,
                    onAddCustomer = onAddCustomer,
                    onShowBottomSheet = onEditCustomer,
                    onCheckCustomerInfo = onCheckCustomerInfo,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun DrawerView(
    onBackup: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onBackup,
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(48.dp)
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Ocean300,
                contentColor = Color.White),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 4.dp,
                pressedElevation = 6.dp,
                disabledElevation = 4.dp,
                hoveredElevation = 6.dp,
                focusedElevation = 6.dp),
            shape = RoundedCornerShape(4.dp),
        ) {
            Text(text = stringResource(id = R.string.backup_title))
        }
    }
}

/**
 * Adds an [NavController.onDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
private fun NavController.currentScreenAsState(): State<Screen> {
    val selectedItem = remember {
        mutableStateOf<Screen>(Screen.ReturnVisitScreen)
    }
    DisposableEffect(key1 = this) {
        val listener = NavController.OnDestinationChangedListener {_, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.ReturnVisitScreen.route } -> {
                    selectedItem.value = Screen.ReturnVisitScreen
                }
                destination.hierarchy.any { it.route == Screen.SuperVip.route } -> {
                    selectedItem.value = Screen.SuperVip
                }
                destination.hierarchy.any { it.route == Screen.NormalVip.route } -> {
                    selectedItem.value = Screen.NormalVip
                }
                destination.hierarchy.any { it.route == Screen.BadCustomer.route } -> {
                    selectedItem.value = Screen.BadCustomer
                }
            }
        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

@Composable
private fun HomeBottomNavigation(
    selectedNavigation: Screen,
    onNavigationSelected: (Screen) -> Unit,
    modifier: Modifier
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.97f),
        contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.surface),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        modifier = modifier
    ) {
        HomeNavigationItems.forEach { item ->
            BottomNavigationItem(
                icon = {
                    HomeNavigationItemIcon(item = item, selected = selectedNavigation == item.screen)
                },
                label = { Text(text = stringResource(id = item.labelResId)) },
                selected = selectedNavigation == item.screen,
                onClick = { onNavigationSelected(item.screen) }
            )
        }
    }
}


@Composable
private fun HomeNavigationItemIcon(item: HomeNavigationItem, selected: Boolean) {
    val painter = when (item) {
        is HomeNavigationItem.ResourceIcon -> painterResource(id = item.iconResId)
        is HomeNavigationItem.ImageVectorIcon -> rememberVectorPainter(image = item.iconImageVector)
    }
    val selectedPainter = when (item) {
        is HomeNavigationItem.ResourceIcon -> item.selectedIconResId?.let { painterResource(it) }
        is HomeNavigationItem.ImageVectorIcon -> item.selectedImageVector?.let { rememberVectorPainter(
            image = it
        ) }
    }
    if (selectedPainter != null) {
        Crossfade(targetState = selected) {
            Icon(
                painter = if (it) selectedPainter else painter,
                contentDescription = stringResource(id = item.contentDescriptionResId)
            )
        }
    } else {
        Icon(
            painter = painter,
            contentDescription = stringResource(id = item.contentDescriptionResId)
        )
    }
}

private sealed class HomeNavigationItem(
    val screen: Screen,
    @StringRes val labelResId: Int,
    @StringRes val contentDescriptionResId: Int
) {
    class ResourceIcon(
        screen: Screen,
        @StringRes labelResId: Int,
        @StringRes contentDescriptionResId: Int,
        @DrawableRes val iconResId: Int,
        @DrawableRes val selectedIconResId: Int? = null
    ) : HomeNavigationItem(screen, labelResId, contentDescriptionResId)

    class ImageVectorIcon(
        screen: Screen,
        @StringRes labelResId: Int,
        @StringRes contentDescriptionResId: Int,
        val iconImageVector: ImageVector,
        val selectedImageVector: ImageVector? = null
    ) : HomeNavigationItem(screen, labelResId, contentDescriptionResId)
}

private val HomeNavigationItems = listOf(
    HomeNavigationItem.ImageVectorIcon(
        screen = Screen.ReturnVisitScreen,
        labelResId = R.string.return_visit_title,
        contentDescriptionResId = R.string.return_visit_title,
        iconImageVector = Icons.Outlined.Star,
        selectedImageVector = Icons.Default.Star
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Screen.SuperVip,
        labelResId = R.string.super_vip,
        contentDescriptionResId = R.string.super_vip,
        iconImageVector = Icons.Outlined.Favorite,
        selectedImageVector = Icons.Default.Favorite
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Screen.NormalVip,
        labelResId = R.string.normal_vip,
        contentDescriptionResId = R.string.normal_vip,
        iconImageVector = Icons.Outlined.Person,
        selectedImageVector = Icons.Default.Person
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Screen.BadCustomer,
        labelResId = R.string.bad_customer,
        contentDescriptionResId = R.string.bad_customer,
        iconImageVector = Icons.Outlined.Warning,
        selectedImageVector = Icons.Default.Warning
    )
)








































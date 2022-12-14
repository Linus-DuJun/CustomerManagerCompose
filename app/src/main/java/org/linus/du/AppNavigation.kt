package org.linus.du

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import org.linus.core.data.db.entities.Customer
import org.linus.du.feature.customer.ui.*

internal sealed class Screen(val route: String) {
    object ReturnVisitScreen: Screen("return_visit")
    object SuperVip: Screen("super_vip")
    object NormalVip: Screen("normal_vip")
    object BadCustomer: Screen("bad_customer")
}

private sealed class LeafScreen(
    private val route: String
) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    object ReturnVisit : LeafScreen("return_visit")
    object SuperVip: LeafScreen("super_vip")
    object NormalVip: LeafScreen("normal_vip")
    object BadCustomer: LeafScreen("bad_customer")
    object ShowCustomerDetails : LeafScreen("show/{customerId}") {
        fun createRoute(root: Screen, customerId: String): String {
            return "${root.route}/show/$customerId"
        }
    }
    object AddSubject: LeafScreen("add_subject")
    object AddReturnVisit: LeafScreen("add_return_visit")
    object AddCustomer: LeafScreen("add_customer")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    onAddCustomer: () -> Unit,
    onCheckCustomerInfo: (Customer) -> Unit,
    onShowBottomSheet: (Customer) -> Unit,
    modifier: Modifier
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.ReturnVisitScreen.route,
        enterTransition = { defaultEnterTransition(initial = initialState, target = targetState) },
        exitTransition = { defaultExitTransition(initialState, targetState) },
        popEnterTransition = { defaultPopEnterTransition() },
        popExitTransition = { defaultPopExitTransition() },
        modifier = modifier
    ) {
        addReturnVisitTopLevel(navController, onAddCustomer = onAddCustomer)
        addSuperVipTopLevel(navController, onAddCustomer = onAddCustomer,  onCheckCustomerInfo = onCheckCustomerInfo, onShowBottomSheet = onShowBottomSheet)
        addNormalVipTopLevel(navController, onAddCustomer = onAddCustomer, onCheckCustomerInfo = onCheckCustomerInfo, onShowBottomSheet = onShowBottomSheet)
        addBadCustomerTopLevel(navController, onAddCustomer = onAddCustomer, onCheckCustomerInfo = onCheckCustomerInfo, onShowBottomSheet = onShowBottomSheet)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addBadCustomerTopLevel(
    navController: NavController,
    onAddCustomer: () -> Unit,
    onShowBottomSheet: (Customer) -> Unit,
    onCheckCustomerInfo: (Customer) -> Unit,
) {
    navigation(
        route = Screen.BadCustomer.route,
        startDestination =  LeafScreen.BadCustomer.createRoute(Screen.BadCustomer)
    ) {
        addBadCustomerScreen(navController, Screen.BadCustomer,
            onAddCustomer = onAddCustomer,
            onShowBottomSheet = onShowBottomSheet,
            onCheckCustomerInfo = onCheckCustomerInfo
        )
        addCustomerDetailScreen(navController, root = Screen.BadCustomer)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addNormalVipTopLevel(
    navController: NavController,
    onAddCustomer: () -> Unit,
    onCheckCustomerInfo: (Customer) -> Unit,
    onShowBottomSheet: (Customer) -> Unit,
) {
    navigation(
        route = Screen.NormalVip.route,
        startDestination = LeafScreen.NormalVip.createRoute(Screen.NormalVip)
    ) {
        addNormalVipScreen(
            navController,
            Screen.NormalVip,
            onAddCustomer = onAddCustomer,
            onCheckCustomerInfo = onCheckCustomerInfo,
            onShowBottomSheet = onShowBottomSheet
        )
        addCustomerDetailScreen(navController, root = Screen.NormalVip)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addSuperVipTopLevel(
    navController: NavController,
    onAddCustomer: () -> Unit,
    onCheckCustomerInfo: (Customer) -> Unit,
    onShowBottomSheet: (Customer) -> Unit
) {
    navigation(
        route = Screen.SuperVip.route,
        startDestination = LeafScreen.SuperVip.createRoute(Screen.SuperVip)
    ) {
        addSuperVipScreen(navController, root = Screen.SuperVip,
            onAddCustomer = onAddCustomer,
            onCheckCustomerInfo = onCheckCustomerInfo,
            onShowBottomSheet = onShowBottomSheet)
        addCustomerDetailScreen(navController, root = Screen.SuperVip)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addReturnVisitTopLevel(
    navController: NavController,
    onAddCustomer: () -> Unit,
) {
    navigation(
        route = Screen.ReturnVisitScreen.route,
        startDestination = LeafScreen.ReturnVisit.createRoute(Screen.ReturnVisitScreen)
    ) {
        addReturnVisitScreen(
            navController = navController,
            root = Screen.ReturnVisitScreen,
            onAddCustomer = onAddCustomer
        )
    }
}


@ExperimentalAnimationApi
private fun NavGraphBuilder.addReturnVisitScreen(
    navController: NavController,
    root: Screen,
    onAddCustomer: () -> Unit
) {
    composable(
        route = LeafScreen.ReturnVisit.createRoute(root),
    ) {
       ReturnVisitScreen(
           refresh = { "refreshing" },
           onAddCustomer = onAddCustomer
       )
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addSuperVipScreen(
    navController: NavController,
    onAddCustomer: () -> Unit,
    onCheckCustomerInfo: (Customer) -> Unit,
    onShowBottomSheet: (Customer) -> Unit,
    root: Screen
) {
    composable(
        route = LeafScreen.SuperVip.createRoute(root)
    ) {
        SuperVipScreen(
            refresh = { "refreshing" },
            onCheckCustomerDetailInfo = onCheckCustomerInfo,
            onAddCustomer = onAddCustomer,
            onShowBottomSheet = onShowBottomSheet
        )
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCustomerDetailScreen(
    navController: NavController,
    root: Screen
) {
    composable(
        route = LeafScreen.ShowCustomerDetails.createRoute(root, "56")
    ) {
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addNormalVipScreen(
    navController: NavController,
    root: Screen,
    onAddCustomer: () -> Unit,
    onCheckCustomerInfo: (Customer) -> Unit,
    onShowBottomSheet: (Customer) -> Unit,
) {
    composable(
        route = LeafScreen.NormalVip.createRoute(root)
    ) {
        NormalVipScreen(
            refresh = { "refreshing" },
            onAddCustomer = onAddCustomer,
            onCheckCustomerDetailInfo = onCheckCustomerInfo,
            onShowBottomSheet = onShowBottomSheet
        )
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addBadCustomerScreen(
    navController: NavController,
    root: Screen,
    onAddCustomer: () -> Unit,
    onCheckCustomerInfo: (Customer) -> Unit,
    onShowBottomSheet: (Customer) -> Unit
) {
    composable(
        route = LeafScreen.BadCustomer.createRoute(root)
    ) {
        BadCustomerScreen(
            refresh = { "refreshing" },
            onAddCustomer = onAddCustomer,
            onShowBottomSheet = onShowBottomSheet,
            onCheckCustomerDetailInfo = onCheckCustomerInfo
        )
    }
}

//@ExperimentalAnimationApi
//private fun NavGraphBuilder.addCustomerScreen(
//    root: Screen
//) {
//    composable(
//        route = LeafScreen.AddCustomer.createRoute(root)
//    ) {
//        AddCustomerScreen(
//            viewModel =
//        )
//    }
//}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph
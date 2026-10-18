package com.example.marketalert.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.marketalert.ShoppingViewModel
import com.example.marketalert.ui.templates.DashboardScreen
import com.example.marketalert.ui.templates.DetailItemScreen
import com.example.marketalert.ui.templates.FormAddShoppingScreen
import com.example.marketalert.ui.templates.MarketFormScreen
import com.example.marketalert.ui.templates.MarketListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: ShoppingViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        // 1. Rute Dashboard (Halaman Utama)
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToMarketList = { navController.navigate(Screen.MarketList.route) },
                onNavigateToMarketForm = { navController.navigate(Screen.MarketForm.route) },
                onNavigateToAddShopping = { navController.navigate(Screen.AddShopping.route) },
                onNavigateToDetail = { storeId: Int ->
                    navController.navigate(Screen.DetailList.createRoute(storeId))
                }
            )
        }

        // 2. Rute Daftar Toko
        composable(route = Screen.MarketList.route) {
            MarketListScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onAddStoreClick = { navController.navigate(Screen.MarketForm.route) },
                onStoreClick = { storeId: Int ->
                    navController.navigate(Screen.DetailList.createRoute(storeId))
                }
            )
        }

        // 3. Rute Form Tambah Toko
        composable(route = Screen.MarketForm.route) {
            MarketFormScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // 4. Rute Form Tambah Item Belanja
        composable(route = Screen.AddShopping.route) {
            FormAddShoppingScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // 5. Rute Detail List (Menerima parameter storeId: Int)
        composable(
            route = Screen.DetailList.route,
            arguments = listOf(navArgument("storeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getInt("storeId") ?: 0
            DetailItemScreen(
                viewModel = viewModel,
                storeId = storeId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

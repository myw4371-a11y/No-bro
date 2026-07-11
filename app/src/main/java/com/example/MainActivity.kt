package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.AppDatabase
import com.example.data.MarketRepository
import com.example.ui.MarketViewModel
import com.example.ui.MarketViewModelFactory
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MarketAppNavigation()
                }
            }
        }
    }
}

@Composable
fun MarketAppNavigation() {
    val navController = rememberNavController()
    
    // Initialize Room DB & Repository in a clean Compose context
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { MarketRepository(database.marketDao()) }
    val viewModel: MarketViewModel = viewModel(
        factory = MarketViewModelFactory(repository)
    )

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // 1. Splash Screen
        composable("splash") {
            SplashScreen(
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // 2. Dashboard
        composable("dashboard") {
            DashboardScreen(
                onNavigateToBazar = { navController.navigate("bazar_select") },
                onNavigateToSavedBazar = { navController.navigate("saved_bazar") },
                onNavigateToMoney = { navController.navigate("money_tracker") },
                onNavigateToMeal = { navController.navigate("meal_tracker") },
                onNavigateToEgg = { navController.navigate("egg_tracker") },
                onNavigateToManager = { navController.navigate("manager") }
            )
        }

        // 3. Bazar Select Screen (Big or Small selector)
        composable("bazar_select") {
            BazarSelectScreen(
                viewModel = viewModel,
                onNavigateToActiveSession = { sessionId ->
                    navController.navigate("bazar_active/$sessionId")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 4. Active Bazar Screen (Item editing / viewing)
        composable(
            route = "bazar_active/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getInt("sessionId") ?: 0
            BazarActiveScreen(
                viewModel = viewModel,
                sessionId = sessionId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 5. Saved Bazar List Screen
        composable("saved_bazar") {
            SavedBazarListScreen(
                viewModel = viewModel,
                onNavigateToSessionDetail = { sessionId ->
                    navController.navigate("bazar_active/$sessionId")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 6. Money Deposits Tracker Screen
        composable("money_tracker") {
            MoneyTrackerScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 7. Meal Consumption Tracker Screen
        composable("meal_tracker") {
            MealTrackerScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 8. Egg Consumption Tracker Screen
        composable("egg_tracker") {
            EggTrackerScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 9. Manager Analytics & Ledger Screen
        composable("manager") {
            ManagerScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

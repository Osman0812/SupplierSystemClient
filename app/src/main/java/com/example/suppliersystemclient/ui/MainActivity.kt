package com.example.suppliersystemclient.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.suppliersystemclient.room.SqlDatabase
import com.example.suppliersystemclient.ui.addsupplierscreen.AddSupplierScreen
import com.example.suppliersystemclient.ui.editsupplierscreen.EditSupplierScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var db: SqlDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(applicationContext, SqlDatabase::class.java, "suppliers.db")
            .build()
        setContent {
            val viewModel: SupplierViewModel = viewModel()
            MainScreen(viewModel)
        }
    }
}

@Composable
fun NavHost(navController: NavController, viewModel: SupplierViewModel) {
    androidx.navigation.compose.NavHost(
        navController = navController as NavHostController,
        startDestination = NavItem.AddSupplier.route
    ) {
        composable(NavItem.AddSupplier.route) { AddSupplierScreen(viewModel) }
        composable(NavItem.EditSupplier.route) { EditSupplierScreen(viewModel) }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: SupplierViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) {
        NavHost(navController = navController, viewModel = viewModel)
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        NavItem.AddSupplier,
        NavItem.EditSupplier,
    )

    BottomAppBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class NavItem(val title: String, val icon: ImageVector, val route: String) {

    object AddSupplier : NavItem("Add", Icons.Default.Add, "addSupplier")
    object EditSupplier : NavItem("Edit", Icons.Default.Edit, "editSupplier")

}

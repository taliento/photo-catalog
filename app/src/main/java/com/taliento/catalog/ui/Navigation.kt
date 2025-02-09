/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taliento.catalog.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.taliento.catalog.model.Catalog
import com.taliento.catalog.ui.catalog.presentation.CatalogScreenGrid
import com.taliento.catalog.ui.catalog.presentation.DetailScreen
import com.taliento.catalog.ui.catalog.presentation.EditPhotoScreen
import com.taliento.catalog.ui.countries.presentation.CountryScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            CountryScreen({
                navController.navigate("catalog")
            })
        }
        composable("catalog") {
            CatalogScreenGrid(Modifier) { photo ->
                navController.navigate(photo)
            }
        }
        composable<Catalog> { navBackStackEntry ->
            val photo: Catalog = navBackStackEntry.toRoute()
            DetailScreen(
                photo,
                goToEditScreen = { navController.navigate("editphoto/${photo.uid}") },
                goBack = { navController.popBackStack() })

        }
        composable(
            "editphoto/{uid}",
            arguments = listOf(
                navArgument("uid") {
                    type = NavType.StringType
                },
            ),
        ) { navBackStackEntry ->

            val uid = navBackStackEntry.arguments?.getString("uid")?.toInt()
            uid?.let {
                EditPhotoScreen(
                    it,
                    goBack = { photo ->
                        navController.navigate(photo) {
                            popUpTo("catalog")
                        }
                    })
            }

        }

    }
}

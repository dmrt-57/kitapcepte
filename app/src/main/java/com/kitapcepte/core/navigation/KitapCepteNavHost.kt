package com.kitapcepte.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kitapcepte.core.designsystem.component.AppBottomBar
import com.kitapcepte.core.designsystem.component.BottomBarDestination
import com.kitapcepte.core.designsystem.component.MemberRequiredBottomSheet
import com.kitapcepte.core.designsystem.theme.BackgroundGradient
import com.kitapcepte.domain.model.Session
import com.kitapcepte.feature.auth.AuthRoute
import com.kitapcepte.feature.cart.CartRoute
import com.kitapcepte.feature.detail.DetailRoute
import com.kitapcepte.feature.favorites.FavoritesRoute
import com.kitapcepte.feature.home.HomeRoute
import com.kitapcepte.feature.onboarding.OnboardingRoute
import com.kitapcepte.feature.payment.PaymentRoute
import com.kitapcepte.feature.profile.ProfileRoute

@Composable
fun KitapCepteNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Screen = Screen.Onboarding,
    session: Session = Session.LoggedOut,
    cartItemCount: Int = 0
) {
    var showMemberRequiredSheet by remember {
        mutableStateOf(false)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val hiddenBottomBarRoutes = listOf(
        "Onboarding",
        "Auth",
        "Detail",
        "Payment",
        "Addresses",
        "Orders",
        "SavedCards",
        "Notifications",
        "ChangePassword",
        "Help",
        "EditProfile"
    )

    val showBottomBar = currentRoute?.let { route ->
        hiddenBottomBarRoutes.none { route.contains(it) }
    } ?: false

    val currentBottomDestination = when {
        currentRoute?.contains("Favorites") == true ->
            BottomBarDestination.FAVORITES

        currentRoute?.contains("Cart") == true ->
            BottomBarDestination.CART

        currentRoute?.contains("Profile") == true ->
            BottomBarDestination.PROFILE

        else ->
            BottomBarDestination.HOME
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundGradient)
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize()
        ) {

            composable<Screen.Onboarding> {
                OnboardingRoute(
                    onNavigateToAuth = {
                        navController.navigate(Screen.Auth) {
                            popUpTo(Screen.Onboarding) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<Screen.Auth> {
                AuthRoute(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Auth) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<Screen.Home> {
                HomeRoute(
                    onNavigateToDetail = { bookId ->
                        navController.navigate(Screen.Detail(bookId))
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile)
                    },
                    onShowMemberRequiredSheet = {
                        showMemberRequiredSheet = true
                    }
                )
            }

            composable<Screen.Detail> {
                DetailRoute(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToDetail = { bookId ->
                        navController.navigate(Screen.Detail(bookId))
                    },
                    onShowMemberRequiredSheet = {
                        showMemberRequiredSheet = true
                    }
                )
            }

            composable<Screen.Favorites> {
                FavoritesRoute(
                    onNavigateToDetail = { bookId ->
                        navController.navigate(Screen.Detail(bookId))
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile)
                    },
                    onShowMemberRequiredSheet = {
                        showMemberRequiredSheet = true
                    }
                )
            }

            composable<Screen.Cart> {
                CartRoute(
                    onNavigateToPayment = {
                        navController.navigate(Screen.Payment)
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile)
                    },
                    onShowMemberRequiredSheet = {
                        showMemberRequiredSheet = true
                    }
                )
            }

            composable<Screen.Payment> {
                PaymentRoute(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Home) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    onShowMemberRequiredSheet = {
                        showMemberRequiredSheet = true
                    }
                )
            }

            composable<Screen.Profile> {
                ProfileRoute(
                    onNavigateToLogin = {
                        navController.navigate(Screen.Auth) {
                            popUpTo(Screen.Profile) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToEditProfile = {
                        navController.navigate(Screen.EditProfile)
                    },
                    onNavigateToAddresses = {
                        navController.navigate(Screen.Addresses)
                    },
                    onNavigateToOrders = {
                        navController.navigate(Screen.Orders)
                    },
                    onNavigateToSavedCards = {
                        navController.navigate(Screen.SavedCards)
                    },
                    onNavigateToFavorites = {
                        navController.navigate(Screen.Favorites)
                    },
                    onNavigateToNotifications = {
                        navController.navigate(Screen.Notifications)
                    },
                    onNavigateToChangePassword = {
                        navController.navigate(Screen.ChangePassword)
                    },
                    onNavigateToHelp = {
                        navController.navigate(Screen.Help)
                    }
                )
            }

            composable<Screen.EditProfile> {
                PlaceholderScreen(
                    title = "Profili Düzenle",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Screen.Addresses> {
                PlaceholderScreen(
                    title = "Adreslerim",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Screen.Orders> {
                PlaceholderScreen(
                    title = "Siparişlerim",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Screen.SavedCards> {
                PlaceholderScreen(
                    title = "Kayıtlı Kartlar",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Screen.Notifications> {
                PlaceholderScreen(
                    title = "Bildirimler",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Screen.ChangePassword> {
                PlaceholderScreen(
                    title = "Şifre Değiştir",
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Screen.Help> {
                PlaceholderScreen(
                    title = "Yardım ve Destek",
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        if (showBottomBar) {
            AppBottomBar(
                currentDestination = currentBottomDestination,
                cartItemCount = cartItemCount,
                onNavigateToDestination = { destination ->

                    when (destination) {

                        BottomBarDestination.HOME -> {
                            navController.navigate(Screen.Home) {
                                popUpTo(
                                    navController.graph.findStartDestination().id
                                ) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }

                        BottomBarDestination.FAVORITES -> {
                            GuestGuard.check(
                                session = session,
                                onGuestRestricted = {
                                    showMemberRequiredSheet = true
                                },
                                onAllowed = {
                                    navController.navigate(Screen.Favorites) {
                                        popUpTo(
                                            navController.graph.findStartDestination().id
                                        ) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }

                        BottomBarDestination.CART -> {
                            GuestGuard.check(
                                session = session,
                                onGuestRestricted = {
                                    showMemberRequiredSheet = true
                                },
                                onAllowed = {
                                    navController.navigate(Screen.Cart) {
                                        popUpTo(
                                            navController.graph.findStartDestination().id
                                        ) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }

                        BottomBarDestination.PROFILE -> {
                            GuestGuard.check(
                                session = session,
                                onGuestRestricted = {
                                    showMemberRequiredSheet = true
                                },
                                onAllowed = {
                                    navController.navigate(Screen.Profile) {
                                        popUpTo(
                                            navController.graph.findStartDestination().id
                                        ) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        if (showMemberRequiredSheet) {
            MemberRequiredBottomSheet(
                onDismissRequest = {
                    showMemberRequiredSheet = false
                },
                onNavigateToAuth = {
                    showMemberRequiredSheet = false
                    navController.navigate(Screen.Auth)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(
    title: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "$title ekranı henüz hazırlanmadı.")
        }
    }
}
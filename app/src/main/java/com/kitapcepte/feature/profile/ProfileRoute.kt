package com.kitapcepte.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileRoute(
    onNavigateToLogin: () -> Unit,
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToAddresses: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToSavedCards: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToChangePassword: () -> Unit = {},
    onNavigateToHelp: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                ProfileUiEffect.NavigateToLogin -> onNavigateToLogin()
                ProfileUiEffect.NavigateToEditProfile -> onNavigateToEditProfile()
                ProfileUiEffect.NavigateToAddresses -> onNavigateToAddresses()
                ProfileUiEffect.NavigateToOrders -> onNavigateToOrders()
                ProfileUiEffect.NavigateToSavedCards -> onNavigateToSavedCards()
                ProfileUiEffect.NavigateToFavorites -> onNavigateToFavorites()
                ProfileUiEffect.NavigateToNotifications -> onNavigateToNotifications()
                ProfileUiEffect.NavigateToChangePassword -> onNavigateToChangePassword()
                ProfileUiEffect.NavigateToHelp -> onNavigateToHelp()
                is ProfileUiEffect.ShowSnackbar -> {
                    // Snackbar can be handled via host
                }
            }
        }
    }

    ProfileScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}
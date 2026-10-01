package com.kitapcepte.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onShowMemberRequiredSheet: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is HomeUiEffect.NavigateToDetail -> onNavigateToDetail(effect.bookId)
                HomeUiEffect.NavigateToProfile -> onNavigateToProfile()
                HomeUiEffect.ShowMemberRequiredSheet -> onShowMemberRequiredSheet()
                is HomeUiEffect.ShowSnackbar -> { /* snackbar can be consumed via host if needed */ }
            }
        }
    }

    HomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

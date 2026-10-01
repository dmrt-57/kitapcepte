package com.kitapcepte

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitapcepte.core.navigation.Screen
import com.kitapcepte.data.local.datastore.UserPreferencesDataStore
import com.kitapcepte.domain.model.Session
import com.kitapcepte.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MainUiState(
    val isLoading: Boolean = true,
    val startDestination: Screen = Screen.Onboarding,
    val session: Session = Session.LoggedOut
)

@HiltViewModel
class MainViewModel @Inject constructor(
    preferencesDataStore: UserPreferencesDataStore,
    sessionRepository: SessionRepository
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = combine(
        preferencesDataStore.isOnboardingCompleted,
        sessionRepository.sessionState
    ) { isOnboardingCompleted, session ->
        val startDestination = when {
            !isOnboardingCompleted -> Screen.Onboarding
            session is Session.LoggedOut -> Screen.Auth
            else -> Screen.Home
        }
        MainUiState(
            isLoading = false,
            startDestination = startDestination,
            session = session
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainUiState(isLoading = true)
    )
}

package com.kitapcepte.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitapcepte.domain.model.Session
import com.kitapcepte.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<ProfileUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        observeSession()
    }

    private fun observeSession() {
        viewModelScope.launch {
            sessionRepository.sessionState.collect { session ->
                when (session) {
                    is Session.LoggedIn -> {
                        _uiState.update {
                            it.copy(
                                name = session.user.name,
                                email = session.user.email,
                                isGuest = false,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }

                    Session.Guest -> {
                        _uiState.update {
                            it.copy(
                                name = "Misafir Kullanıcı",
                                email = "",
                                isGuest = true,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }

                    Session.LoggedOut -> {
                        _uiState.update {
                            it.copy(
                                name = "",
                                email = "",
                                isGuest = false,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.EditProfileClicked -> {
                sendEffect(ProfileUiEffect.NavigateToEditProfile)
            }

            ProfileUiEvent.AddressesClicked -> {
                sendEffect(ProfileUiEffect.NavigateToAddresses)
            }

            ProfileUiEvent.OrdersClicked -> {
                sendEffect(ProfileUiEffect.NavigateToOrders)
            }

            ProfileUiEvent.SavedCardsClicked -> {
                sendEffect(ProfileUiEffect.NavigateToSavedCards)
            }

            ProfileUiEvent.FavoritesClicked -> {
                sendEffect(ProfileUiEffect.NavigateToFavorites)
            }

            ProfileUiEvent.NotificationsClicked -> {
                sendEffect(ProfileUiEffect.NavigateToNotifications)
            }

            ProfileUiEvent.ChangePasswordClicked -> {
                sendEffect(ProfileUiEffect.NavigateToChangePassword)
            }

            ProfileUiEvent.HelpClicked -> {
                sendEffect(ProfileUiEffect.NavigateToHelp)
            }

            ProfileUiEvent.LogoutClicked -> {
                logout()
            }

            ProfileUiEvent.DismissError -> {
                _uiState.update {
                    it.copy(errorMessage = null)
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            sessionRepository.logout()
            sendEffect(ProfileUiEffect.NavigateToLogin)
        }
    }

    private fun sendEffect(effect: ProfileUiEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
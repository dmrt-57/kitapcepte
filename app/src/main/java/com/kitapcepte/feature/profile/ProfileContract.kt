package com.kitapcepte.feature.profile

import com.kitapcepte.core.common.UiText

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val isGuest: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)

sealed interface ProfileUiEvent {
    data object EditProfileClicked : ProfileUiEvent
    data object AddressesClicked : ProfileUiEvent
    data object OrdersClicked : ProfileUiEvent
    data object SavedCardsClicked : ProfileUiEvent
    data object FavoritesClicked : ProfileUiEvent
    data object NotificationsClicked : ProfileUiEvent
    data object ChangePasswordClicked : ProfileUiEvent
    data object HelpClicked : ProfileUiEvent
    data object LogoutClicked : ProfileUiEvent
    data object DismissError : ProfileUiEvent
}

sealed interface ProfileUiEffect {
    data object NavigateToEditProfile : ProfileUiEffect
    data object NavigateToAddresses : ProfileUiEffect
    data object NavigateToOrders : ProfileUiEffect
    data object NavigateToSavedCards : ProfileUiEffect
    data object NavigateToFavorites : ProfileUiEffect
    data object NavigateToNotifications : ProfileUiEffect
    data object NavigateToChangePassword : ProfileUiEffect
    data object NavigateToHelp : ProfileUiEffect
    data object NavigateToLogin : ProfileUiEffect
    data class ShowSnackbar(val message: UiText) : ProfileUiEffect
}
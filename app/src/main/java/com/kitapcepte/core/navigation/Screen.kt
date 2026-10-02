package com.kitapcepte.core.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Onboarding : Screen

    @Serializable
    data object Auth : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data class Detail(val bookId: String) : Screen

    @Serializable
    data object Favorites : Screen

    @Serializable
    data object Cart : Screen

    @Serializable
    data object Payment : Screen

    @Serializable
    data object Profile : Screen

    // --- Eksik Olan Profil Alt Sayfaları ---

    @Serializable
    data object EditProfile : Screen

    @Serializable
    data object Addresses : Screen

    @Serializable
    data object Orders : Screen

    @Serializable
    data object SavedCards : Screen

    @Serializable
    data object Notifications : Screen

    @Serializable
    data object ChangePassword : Screen

    @Serializable
    data object Help : Screen
}
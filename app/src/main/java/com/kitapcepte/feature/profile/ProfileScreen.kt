package com.kitapcepte.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Profil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(58.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Profil",
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.size(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.name.ifBlank { "Kullanıcı" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (state.isGuest) "Misafir hesap" else state.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "Düzenle",
                    modifier = Modifier
                        .clickable { onEvent(ProfileUiEvent.EditProfileClicked) }
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        ProfileSectionTitle("Hesabım")

        ProfileMenuItem(
            icon = Icons.Outlined.LocationOn,
            title = "Adreslerim",
            subtitle = "Teslimat adreslerini yönet",
            onClick = { onEvent(ProfileUiEvent.AddressesClicked) }
        )

        ProfileMenuItem(
            icon = Icons.Outlined.ShoppingBag,
            title = "Siparişlerim",
            subtitle = "Siparişlerini ve durumlarını görüntüle",
            onClick = { onEvent(ProfileUiEvent.OrdersClicked) }
        )

        ProfileMenuItem(
            icon = Icons.Outlined.CreditCard,
            title = "Kayıtlı Kartlar",
            subtitle = "Ödeme yöntemlerini yönet",
            onClick = { onEvent(ProfileUiEvent.SavedCardsClicked) }
        )

        ProfileMenuItem(
            icon = Icons.Outlined.FavoriteBorder,
            title = "Favorilerim",
            subtitle = "Favori kitaplarını görüntüle",
            onClick = { onEvent(ProfileUiEvent.FavoritesClicked) }
        )

        ProfileSectionTitle("Ayarlar")

        ProfileMenuItem(
            icon = Icons.Outlined.Notifications,
            title = "Bildirimler",
            subtitle = "Bildirim tercihlerini yönet",
            onClick = { onEvent(ProfileUiEvent.NotificationsClicked) }
        )

        ProfileMenuItem(
            icon = Icons.Outlined.Lock,
            title = "Şifre Değiştir",
            subtitle = "Hesap güvenliğini yönet",
            onClick = { onEvent(ProfileUiEvent.ChangePasswordClicked) }
        )

        ProfileMenuItem(
            icon = Icons.Outlined.HelpOutline,
            title = "Yardım ve Destek",
            subtitle = "Sık sorulan sorular ve destek",
            onClick = { onEvent(ProfileUiEvent.HelpClicked) }
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEvent(ProfileUiEvent.LogoutClicked) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = "Çıkış Yap",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )

                Spacer(modifier = Modifier.size(14.dp))

                Text(
                    text = "Çıkış Yap",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Alttaki yüzen menünün "Çıkış Yap" kartını kapatmaması için boşluk
        Spacer(modifier = Modifier.height(110.dp))
    }
}

@Composable
private fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 4.dp, top = 8.dp)
    )
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.padding(11.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.size(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
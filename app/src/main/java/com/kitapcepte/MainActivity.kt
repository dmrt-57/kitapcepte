package com.kitapcepte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kitapcepte.core.designsystem.theme.KitapCepteTheme
import com.kitapcepte.core.navigation.KitapCepteNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KitapCepteTheme {
                KitapCepteNavHost()
            }
        }
    }
}

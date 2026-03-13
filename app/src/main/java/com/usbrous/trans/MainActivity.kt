package com.usbrous.trans

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.usbrous.trans.navigation.TransNavHost
import com.usbrous.trans.ui.theme.TransTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TransTheme {
                val navController = rememberNavController()
                TransNavHost(navController = navController)
            }
        }
    }
}

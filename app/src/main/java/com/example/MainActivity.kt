package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.screens.MainNavigationScreen
import com.example.ui.theme.SlopeWatchTheme
import com.example.ui.viewmodel.SlopeWatchViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SlopeWatchTheme {
                val viewModel: SlopeWatchViewModel = koinViewModel()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = com.example.ui.theme.PureWhite
                ) {
                    MainNavigationScreen(viewModel = viewModel)
                }
            }
        }
    }
}

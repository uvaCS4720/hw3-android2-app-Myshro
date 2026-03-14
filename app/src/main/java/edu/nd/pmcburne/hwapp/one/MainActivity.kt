package edu.nd.pmcburne.hwapp.one

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.nd.pmcburne.hwapp.one.ui.theme.HWStarterRepoTheme
import edu.nd.pmcburne.hwapp.one.ui.BasketballViewModel

class MainActivity : ComponentActivity() {
    val viewModel: BasketballViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HWStarterRepoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Text(
                            text = "Hi",
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}


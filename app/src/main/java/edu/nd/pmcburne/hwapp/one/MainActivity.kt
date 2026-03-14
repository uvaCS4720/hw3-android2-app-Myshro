package edu.nd.pmcburne.hwapp.one

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.nd.pmcburne.hwapp.one.data.database.AppDatabase
import edu.nd.pmcburne.hwapp.one.model.Gender
import edu.nd.pmcburne.hwapp.one.network.BasketballApiService
import edu.nd.pmcburne.hwapp.one.data.repository.BasketballRepository
import edu.nd.pmcburne.hwapp.one.ui.components.GameCard
import edu.nd.pmcburne.hwapp.one.ui.theme.HWStarterRepoTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    private val database by lazy {
        androidx.room.Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "games"
        ).build()
    }

    private val apiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://ncaa-api.henrygd.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BasketballApiService::class.java)
    }

    private val viewModel: BasketballViewModel by viewModels {
        BasketballViewModelFactory(
            repository = BasketballRepository(
                context = applicationContext,
                api = apiService,
                gameDao = database.gameDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HWStarterRepoTheme {
                BasketballScoresApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasketballScoresApp(
    viewModel: BasketballViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("College Basketball Scores") },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        },
        // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#Snackbar(androidx.compose.ui.Modifier,kotlin.Function0,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,kotlin.Function0)
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                viewModel.onDateSelected(
                    java.time.LocalDate.of(year, month + 1, dayOfMonth)
                )
            },
            uiState.selectedDate.year,
            uiState.selectedDate.monthValue - 1,
            uiState.selectedDate.dayOfMonth
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { datePickerDialog.show() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null
                        )
                    }

                    ElevatedAssistChip(
                        onClick = { datePickerDialog.show() },
                        label = {
                            Text(uiState.selectedDate.format(dateFormatter))
                        }
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilterChip(
                        selected = uiState.selectedGender == Gender.MEN,
                        onClick = { viewModel.onGenderSelected(Gender.MEN) },
                        label = { Text("Men") }
                    )

                    FilterChip(
                        selected = uiState.selectedGender == Gender.WOMEN,
                        onClick = { viewModel.onGenderSelected(Gender.WOMEN) },
                        label = { Text("Women") }
                    )
                }

                if (uiState.games.isEmpty() && !uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No games found for this selection.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.games,
                            key = { it.gameId + it.dateKey + it.gender }
                        ) { game ->
                            GameCard(game = game)
                        }
                    }
                }
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
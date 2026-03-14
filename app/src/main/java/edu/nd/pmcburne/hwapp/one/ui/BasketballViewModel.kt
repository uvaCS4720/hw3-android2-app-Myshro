package edu.nd.pmcburne.hwapp.one

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hwapp.one.model.Game
import edu.nd.pmcburne.hwapp.one.model.Gender
import edu.nd.pmcburne.hwapp.one.data.repository.BasketballRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class BasketballUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedGender: Gender = Gender.MEN,
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/***************************************************************************************
 * REFERENCES
 * URL: https://developer.android.com/topic/libraries/architecture/viewmodel
 * Software License: Apache License, Version 2.0
 ***************************************************************************************/

class BasketballViewModel(
    private val repository: BasketballRepository
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())
    private val selectedGender = MutableStateFlow(Gender.MEN)
    private val isLoading = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    private val gamesFlow = combine(selectedDate, selectedGender) { date, gender ->
        date to gender
    }.flatMapLatest { (date, gender) ->
        repository.observeGames(date, gender.apiValue)
    }

    val uiState: StateFlow<BasketballUiState> =
        combine(
            selectedDate,
            selectedGender,
            gamesFlow,
            isLoading,
            errorMessage
        ) { date, gender, games, loading, error ->
            BasketballUiState(
                selectedDate = date,
                selectedGender = gender,
                games = games,
                isLoading = loading,
                errorMessage = error
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BasketballUiState()
        )

    init {
        refresh()
    }

    fun onDateSelected(date: LocalDate) {
        selectedDate.value = date
        refresh()
    }

    fun onGenderSelected(gender: Gender) {
        selectedGender.value = gender
        refresh()
    }

    fun clearError() {
        errorMessage.value = null
    }

    fun refresh() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            val result = repository.refresh(
                date = selectedDate.value,
                gender = selectedGender.value.apiValue
            )

            result.exceptionOrNull()?.let { ex ->
                errorMessage.value = ex.message ?: "Something went wrong."
            }

            isLoading.value = false
        }
    }
}

/***************************************************************************************
 * REFERENCES
 * URL: https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories
 * Software License: Apache License, Version 2.0
 ***************************************************************************************/

class BasketballViewModelFactory(
    private val repository: BasketballRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BasketballViewModel::class.java)) {
            return BasketballViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
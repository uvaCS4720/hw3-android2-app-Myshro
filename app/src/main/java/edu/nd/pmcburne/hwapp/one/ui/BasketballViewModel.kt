package edu.nd.pmcburne.hwapp.one.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hwapp.one.data.repository.BasketballRepository
import edu.nd.pmcburne.hwapp.one.model.Game
import kotlinx.coroutines.launch

class BasketballViewModel(
    private val repository : BasketballRepository
) : ViewModel() {

    var games by mutableStateOf<List<Game>>(emptyList())
    var loading by mutableStateOf(false)

    fun loadGames(date: String, gender: String) {
        viewModelScope.launch {
            loading = true
            games = repository.getGames(date, gender)
            loading = false
        }
    }
}
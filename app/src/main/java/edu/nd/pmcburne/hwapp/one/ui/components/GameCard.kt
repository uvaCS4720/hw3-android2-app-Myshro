package edu.nd.pmcburne.hwapp.one.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import edu.nd.pmcburne.hwapp.one.model.Game


@Composable
fun GameCard(game: Game) {

    Column {
        Text("${game.homeTeam} ${game.homeScore}")
        Text("${game.awayTeam} ${game.awayScore}")

        if (game.status == "final") {
            Text("Final")
        } else {
            Text("${game.period} ${game.clock}")
        }
    }
}
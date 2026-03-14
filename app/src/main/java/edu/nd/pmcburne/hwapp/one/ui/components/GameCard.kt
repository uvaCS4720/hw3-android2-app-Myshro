package edu.nd.pmcburne.hwapp.one.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.nd.pmcburne.hwapp.one.model.Game
import edu.nd.pmcburne.hwapp.one.model.GameStatus

@Composable
fun GameCard(
    game: Game,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TeamRow(
                label = "Away",
                team = game.awayTeam,
                score = if (game.status == GameStatus.UPCOMING) "-" else game.awayScore,
                winner = game.awayWinner
            )

            TeamRow(
                label = "Home",
                team = game.homeTeam,
                score = if (game.status == GameStatus.UPCOMING) "-" else game.homeScore,
                winner = game.homeWinner
            )

            Text(
                text = game.statusLine,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (game.status == GameStatus.FINAL && game.winnerLabel.isNotBlank()) {
                Text(
                    text = game.winnerLabel,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun TeamRow(
    label: String,
    team: String,
    score: String,
    winner: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = team,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (winner) FontWeight.Bold else FontWeight.Normal
            )
        }

        Text(
            text = score,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (winner) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}
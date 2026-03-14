package edu.nd.pmcburne.hwapp.one.network

/***************************************************************************************
 * REFERENCES
 * URL: https://square.github.io/retrofit/
 * Software License: Apache License, Version 2.0
 ***************************************************************************************/

import retrofit2.http.GET
import retrofit2.http.Path

interface BasketballApiService {

    @GET("scoreboard/basketball-{gender}/d1/{year}/{month}/{day}")
    suspend fun getGames(
        @Path("gender") gender: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String
    )
}
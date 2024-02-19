package com.kaylr.superhero_room

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("search/sp")
    // -> /api/7038753112847970/ delante si no va
    //@Path("name") superheroName:String
    suspend fun getSuperheroes(): Response<SuperHeroDataResponse>
    //si usa corrutinas hay que usar "suspend"

    @GET("search/sp")
    // -> /api/7038753112847970/ delante si no va
    //@Path("id") superheroId:String
    suspend fun getSuperheroDetail():Response<SuperHeroDetailResponse>

}
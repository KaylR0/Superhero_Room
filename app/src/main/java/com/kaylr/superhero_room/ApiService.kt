package com.kaylr.superhero_room

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("search/{name}")
    // -> /api/7038753112847970/ delante si no va
    suspend fun getSuperheroes(@Path("name") superheroName:String): Response<SuperHeroDataResponse>
    //si usa corrutinas hay que usar "suspend"

    @GET("{id}")
    // -> /api/7038753112847970/ delante si no va
    suspend fun getSuperheroDetail(@Path("id") superheroId:String):Response<SuperHeroDetailResponse>

}
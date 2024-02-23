package com.kaylr.superhero_room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.kaylr.superhero_room.bbdd.HeroDao
import com.kaylr.superhero_room.bbdd.HeroEntity
import com.kaylr.superhero_room.bbdd.SuperheroDatabase
import com.kaylr.superhero_room.bbdd.toDatabase
import com.kaylr.superhero_room.databinding.ActivitySuperHeroBinding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SuperHeroActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "extra_id"
        const val MY_TOKEN = "7038753112847970"
    }
    private lateinit var binding: ActivitySuperHeroBinding
    private lateinit var retrofit: Retrofit
    private lateinit var adapter: SuperHeroAdapter
    private lateinit var room: SuperheroDatabase
    private fun navigateToDetail(id: String) {
        val intent = Intent(this, DetailSuperHeroActivity::class.java)
        intent.putExtra(EXTRA_ID, id)
        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        room = getRoom()
        binding = ActivitySuperHeroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofit = getRetrofit()
        fillDatabase()
        initUI()
    }

    private fun initUI() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchByName(query.orEmpty())
                return false
            }
            override fun onQueryTextChange(newText: String?) = false
        })
        adapter = SuperHeroAdapter{superheroId -> navigateToDetail(superheroId)}
        binding.rvSuperHero.setHasFixedSize(true)
        binding.rvSuperHero.layoutManager = LinearLayoutManager(this)
        binding.rvSuperHero.adapter = adapter
    }

    private fun searchByName(query: String) {//query: String
        binding.progressBar.isVisible = true

        //.IO es para hilos secundarios
        //.MAIN es para el hilo principal
        CoroutineScope(Dispatchers.IO).launch {
            //usamos corrutinas para que use otro hilo y que no se atasque el programa principal
            val myResponse: Response<SuperHeroDataResponse> =
                retrofit.create(ApiService::class.java).getSuperheroes()//(query)
            if (myResponse.isSuccessful) {
                Log.i("Consulta", "Funciona :)")
                val response: SuperHeroDataResponse? = myResponse.body()
                if (response != null) {
                    Log.i("Cuerpo de la consulta", response.toString())
                    runOnUiThread {
                        //adapter.updateList(response.superheroes)
                        //binding.progressBar.isVisible = false
                    }
                }
            } else {
                Log.i("Consulta", "No funciona :(")
            }
        }
    }
    private fun fillDatabase() {
        binding.progressBar.isVisible = true

        //.IO es para hilos secundarios
        //.MAIN es para el hilo principal
        CoroutineScope(Dispatchers.IO).launch {
            //usamos corrutinas para que use otro hilo y que no se atasque el programa principal
            val myResponse: Response<SuperHeroDataResponse> =
                retrofit.create(ApiService::class.java).getSuperheroes()//(query)
            if (myResponse.isSuccessful) {
                Log.i("Consulta", "Funciona :)")
                val response: SuperHeroDataResponse? = myResponse.body()
                if (response != null) {
                    Log.i("Cuerpo de la consulta", response.toString())
                        val list = response.superheroes.map { it.toDatabase() }
                        room.getHeroDao().deleteAllSuperheroes()
                        room.getHeroDao().insertAll(list)
                        room.getHeroDao().update(list)

                       // if (room.getHeroDao().getAllSuperheroes().isEmpty()) {
                        adapter.updateList(room.getHeroDao().getAllSuperheroes())


                        /* val heroes = room.getHeroDao().getAllSuperheroes()
                        for (i in heroes.indices) {
                            println(heroes[i])
                        }
                        Log.i("BASE DE DATOS", room.getHeroDao().getSuperheroes(1).toString())*/
                    runOnUiThread{
                        binding.progressBar.isVisible = false
                    }
                }
            } else {
                Log.i("Consulta", "No funciona :(")
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://superheroapi.com/api/$MY_TOKEN/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun getRoom(): SuperheroDatabase{
       return Room
            .databaseBuilder(this, SuperheroDatabase::class.java, "superheroes")
            .build()
    }
}
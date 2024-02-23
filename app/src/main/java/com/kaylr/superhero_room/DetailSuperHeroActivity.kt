package com.kaylr.superhero_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.net.toUri
import androidx.room.Room
import com.kaylr.superhero_room.R
import com.kaylr.superhero_room.databinding.ActivityDetailSuperHeroBinding
import com.kaylr.superhero_room.SuperHeroActivity.Companion.EXTRA_ID
import com.kaylr.superhero_room.SuperHeroActivity.Companion.MY_TOKEN
import com.kaylr.superhero_room.bbdd.DetailsEntity
import com.kaylr.superhero_room.bbdd.SuperheroDatabase
import com.kaylr.superhero_room.bbdd.toDatabase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt


class DetailSuperHeroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSuperHeroBinding
    private lateinit var room: SuperheroDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        room = getRoom()
        room.getDetailsDao()
        setContentView(R.layout.activity_detail_super_hero)
        binding = ActivityDetailSuperHeroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var id: String = intent.getStringExtra(EXTRA_ID).orEmpty()
        getSuperheroInformation(id.toInt())//id
    }

    private fun getSuperheroInformation(id: Int) {//id: String
        CoroutineScope(Dispatchers.IO).launch {
            val superheroDetail: Response<SuperHeroDetailResponse> =
                getRetrofit().create(ApiService::class.java).getSuperheroDetail()//(id)
            if (superheroDetail.isSuccessful) {
                val response: SuperHeroDetailResponse? = superheroDetail.body()
                if (response != null) {
                    val list = response.details.map { it.toDatabase() }
                    room.getDetailsDao().deleteAllDetails()
                    room.getDetailsDao().insertAll(list)
                    room.getDetailsDao().update(list)

                    val listDetails = room.getDetailsDao().getDetails(id)
                    //val list = room.getDetailsDao().getAllDetails()
                    runOnUiThread {
                    //ARREGLAR PARA QUE MUESTRE DETALLES DEL HEROE SELECCIONADO
                        //createUI(listDetails)

                    }
                    //se pone !! cuando estamos seguros de que es nulo
                }
            }
        }
    }

    private fun createUI(listDetails: List<DetailsEntity>){
        CoroutineScope(Dispatchers.IO).launch {
            Picasso.get().load(room.getDetailsDao().getHeroImage(listDetails[0].id))
                .into(binding.ivSuperhero)
            binding.tvSuperheroName.text = room.getDetailsDao().getHeroImage(listDetails[0].id)
            binding.tvSuperheroRealName.text = listDetails[0].fullName
            binding.tvPublisher.text = listDetails[0].publisher
            prepareStats(listDetails)//superHero.powerstats
        }
    }
    private fun prepareStats(superHero: List<DetailsEntity>) {//powerstats: PowerStatsResponse
        updateHeight(binding.viewIntelligence, superHero[0].intelligence.orEmpty())
       // updateHeight(binding.viewStrength, superHero.strength?.toString().orEmpty())
       // updateHeight(binding.viewSpeed, superHero.speed?.toString().orEmpty())
       // updateHeight(binding.viewDurability, superHero.durability?.toString().orEmpty())
      //  updateHeight(binding.viewPower, superHero.power?.toString().orEmpty())
       // updateHeight(binding.viewCombat, superHero.combat?.toString().orEmpty())
    }
    private fun updateHeight(view: View, stat:String){
        val params = view.layoutParams
        if(stat == "null"){
            params.height = 0
        }else {
            params.height = pxToDp(stat.toFloat())
        }
        view.layoutParams = params
    }
    private fun pxToDp(px:Float):Int{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.displayMetrics).roundToInt()
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
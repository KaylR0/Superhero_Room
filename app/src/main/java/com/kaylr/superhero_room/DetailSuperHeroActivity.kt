package com.kaylr.superhero_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import com.kaylr.superhero_room.R
import com.kaylr.superhero_room.databinding.ActivityDetailSuperHeroBinding
import com.kaylr.superhero_room.SuperHeroActivity.Companion.EXTRA_ID
import com.kaylr.superhero_room.SuperHeroActivity.Companion.MY_TOKEN
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt


class DetailSuperHeroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSuperHeroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_super_hero)
        binding = ActivityDetailSuperHeroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var id: String = intent.getStringExtra(EXTRA_ID).orEmpty()
        getSuperheroInformation(id)
    }

    private fun getSuperheroInformation(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val superheroDetail =
                getRetrofit().create(ApiService::class.java).getSuperheroDetail()//(id)
            if(superheroDetail.body() != null){
                runOnUiThread{ createUI(superheroDetail.body()!!) }
                //se pone !! cuando estamos seguros de que es nulo
            }
        }
    }

    private fun createUI(superHero: SuperheroIdResponse){
        //Picasso.get().load(superHero.image.url).into(binding.ivSuperhero)
       // binding.tvSuperheroName.text = superHero.name
        binding.tvSuperheroRealName.text = superHero.biography.fullName
        binding.tvPublisher.text = superHero.biography.publisher
        prepareStats(superHero.powerstats)
    }
    private fun prepareStats(powerstats: PowerStatsResponse) {
        updateHeight(binding.viewIntelligence, powerstats.intelligence?.toString().orEmpty())
        updateHeight(binding.viewStrength, powerstats.strength?.toString().orEmpty())
        updateHeight(binding.viewSpeed, powerstats.speed?.toString().orEmpty())
        updateHeight(binding.viewDurability, powerstats.durability?.toString().orEmpty())
        updateHeight(binding.viewPower, powerstats.power?.toString().orEmpty())
        updateHeight(binding.viewCombat, powerstats.combat?.toString().orEmpty())
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

}
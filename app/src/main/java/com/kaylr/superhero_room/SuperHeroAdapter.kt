package com.kaylr.superhero_room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaylr.superhero_room.R
import com.kaylr.superhero_room.bbdd.HeroEntity

class SuperHeroAdapter( private var superheroList: List<HeroEntity> = emptyList(), private val navigateToDetailActivity: (String) -> Unit)
    : RecyclerView.Adapter<SuperheroViewHolder>() {
    fun updateList(list: List<HeroEntity>) {
        this.superheroList = list
        //notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroViewHolder {
        return SuperheroViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_superhero, parent, false)
        )
    }
    override fun onBindViewHolder(holder: SuperheroViewHolder, position: Int) {
        holder.bind(superheroList[position],navigateToDetailActivity)
    }
    override fun getItemCount() = superheroList.size
}

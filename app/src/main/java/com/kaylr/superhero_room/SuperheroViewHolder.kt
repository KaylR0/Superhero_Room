package com.kaylr.superhero_room

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kaylr.superhero_room.databinding.ItemSuperheroBinding
import com.squareup.picasso.Picasso

class SuperheroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemSuperheroBinding.bind(view)
    fun bind(superheroItemResponse: SuperheroItemResponse, navigateToDetailActivity: (String) -> Unit) {
        //recibe los items de la lista
        Picasso.get().load(superheroItemResponse.superheroImage.url).into(binding.ivSuperHero);
        binding.tvSuperHeroName.text = superheroItemResponse.name
        binding.root.setOnClickListener { navigateToDetailActivity(superheroItemResponse.superheroId) }
        //al ponerle root le decimos que hacemos referencia a cualquier parte del layout. Bien pulsemos el texto o la imagen, hará el setOnClick.
    }
}

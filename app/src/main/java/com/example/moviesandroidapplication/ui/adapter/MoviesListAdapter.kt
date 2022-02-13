package com.example.moviesandroidapplication.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesandroidapplication.databinding.ItemMovieRowBinding
import com.example.moviesandroidapplication.model.Movies
import com.bumptech.glide.Glide
import com.example.moviesandroidapplication.R
import com.example.moviesandroidapplication.entities.MoviesTable
import com.example.moviesandroidapplication.repository.LocalRepository
import com.example.moviesandroidapplication.utils.Callbacks.MovieLikedListener


class MoviesListAdapter (private val moviesLikedListener: MovieLikedListener, private val likedMovies: List<MoviesTable>?) :
    RecyclerView.Adapter<MoviesListAdapter.MoviesViewHolder>() {


    inner class MoviesViewHolder(val itemMovieRowBinding: ItemMovieRowBinding) :
        RecyclerView.ViewHolder(
            itemMovieRowBinding.root
        ) {
        init {
            itemMovieRowBinding.imgLike.setOnClickListener {
                val position = adapterPosition
                // TODO: 2022-02-12 add here call back for list on click listener
                itemMovieRowBinding.imgLike.setImageResource(R.drawable.ic_liked)
                Toast.makeText(
                    itemMovieRowBinding.imageView.context,
                    "You Liked ${differ.currentList[position].title} movie",
                    Toast.LENGTH_SHORT
                ).show()
                differ.currentList[position].let {
                    moviesLikedListener.movieLikedCallback(it)
                }

                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            ItemMovieRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        with(holder)
        {
            with(differ.currentList[position]) {
                itemMovieRowBinding.txtName.text = title
                itemMovieRowBinding.txtReleaseDate.text = "$original_language | $release_date"
                itemMovieRowBinding.txtOverview.text = overview

                poster_path.let {
                    Glide.with(itemMovieRowBinding.imageView.context)
                        .asBitmap()
                        .load(posterURL())
                        .into(itemMovieRowBinding.imageView);
                }

                // Liked Movies
                likedMovies.let { list ->


                    list?.forEach {
                        if (id.equals(it.id)){
                            itemMovieRowBinding.imgLike.setImageResource(R.drawable.ic_liked)
                        } else {
                            itemMovieRowBinding.imgLike.setImageResource(R.drawable.ic_like)
                        }
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    /***************************** Use the differ to put data in listview  **********************************/

    private val differcallback = object : DiffUtil.ItemCallback<Movies>() {
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movies, newItem: Movies): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differcallback)

}
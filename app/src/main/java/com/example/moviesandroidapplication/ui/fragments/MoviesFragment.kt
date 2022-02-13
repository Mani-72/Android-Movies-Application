package com.example.moviesandroidapplication.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesandroidapplication.R
import com.example.moviesandroidapplication.databinding.FragmentMoviesBinding
import com.example.moviesandroidapplication.ui.adapter.MoviesListAdapter
import com.example.moviesandroidapplication.ui.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.ContextCompat.getSystemService

import android.app.SearchManager
import android.content.Context
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.moviesandroidapplication.entities.MoviesTable
import com.example.moviesandroidapplication.model.Movies
import com.example.moviesandroidapplication.repository.LocalRepository
import com.example.moviesandroidapplication.utils.Callbacks.MovieLikedListener
import javax.inject.Inject


@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies), MovieLikedListener {

    private val moviesViewModel: MoviesViewModel by viewModels()
    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesListAdapter: MoviesListAdapter
    private var likedMovies: List<MoviesTable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviesBinding.bind(view)

        getLikedMovies() // From Database
        getAllEntries() // From WebService API
        moviesViewModel.getPopularMovies()
    }

    private fun getLikedMovies() {
        moviesViewModel.likedMoviesLiveData.observe(viewLifecycleOwner) {
            likedMovies = it
            moviesViewModel.likedMoviesLiveData.removeObservers(viewLifecycleOwner)
            initializeRecyclerView()
        }
    }

    private fun initializeRecyclerView() {
        moviesListAdapter = MoviesListAdapter(this, likedMovies) // add the callback listener later
        binding.rcvMovies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moviesListAdapter
        }
    }

    private fun getAllEntries() {
        moviesViewModel.moviesLiveData.observe(this, {
            moviesListAdapter.differ.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchItem.let {
            val searchView = searchItem.actionView as SearchView
            with(searchView) {
                setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query.let {
                            moviesViewModel.getSearchedMovies(query!!)
                            clearFocus()
                            return true
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        return true
                    }
                })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.popular -> moviesViewModel.getPopularMovies()
            R.id.upcoming -> moviesViewModel.getUpcomingMovies()
            R.id.like -> {
              showLikedMovies()
            }
            else -> return false
        }

        return true
    }

    private fun showLikedMovies() {
        var result: MutableList<Movies> = mutableListOf<Movies>()
        likedMovies.let {
            it?.forEach { movieTable ->
                result.add(
                    Movies(
                        id = movieTable.id,
                        title = movieTable.title,
                        release_date = movieTable.release_date,
                        poster_path = movieTable.poster_path,
                        overview = movieTable.overview,
                        original_language = movieTable.original_language,
                        backdrop_path = movieTable.backdrop_path,
                        adult = movieTable.adult
                    )
                )
            }
            result.let {
                moviesListAdapter.differ.submitList(it)
            }

        }
    }

    override fun movieLikedCallback(movie: Movies) {
        moviesViewModel.saveLikedMovies(movie)
    }


}


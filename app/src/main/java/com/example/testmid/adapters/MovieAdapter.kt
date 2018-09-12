package com.example.testmid.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testmid.R
import com.example.testmid.models.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieAdapter(var items: ArrayList<Movie>, val context: Context) : RecyclerView.Adapter<MovieHolder>() {


    // Gets the number of status in the list
    override fun getItemCount(): Int {
        return items.size
    }


    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: MovieHolder, position: Int) = holder.bind(items[position])


    fun update(items: ArrayList<Movie>?) {
        items?.forEach { item -> this.items.add(item) }
        notifyDataSetChanged()
    }

    fun search(keyword: String,movies: ArrayList<Movie>) {
        val defaultMovies=ArrayList<Movie>()
        defaultMovies.addAll(movies)
        val i = defaultMovies.iterator()
        while (i.hasNext()) {
            val movie = i.next() // must be called before you can call i.remove()
            // Do something
            if(!movie.title.toLowerCase().contains(keyword.toLowerCase()))
            i.remove()
        }
        this.items.clear()
        this.items.addAll(defaultMovies)
        notifyDataSetChanged()
    }
}


class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
    val picture = view.picture
    val title = view.txt_title
    val year = view.txt_year
    val overview = view.txt_overview
    fun bind(movie: Movie) = with(itemView) {
        //show the title
        title.text = movie.title
        //show the year
        year.text = movie.release_date
        //show the overview
        overview.text = movie.overview
        //show the picture
        Picasso.get().load("https://image.tmdb.org/t/p/w185_and_h278_bestv2"+movie.poster_path).into(picture)
    }
}

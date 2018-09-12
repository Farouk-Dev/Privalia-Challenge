package com.example.testmid.activities.list_movies_activity.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.testmid.R
import com.example.testmid.activities.list_movies_activity.interfaces.ViewInterface
import com.example.testmid.activities.list_movies_activity.interfaces.WsCallInterface
import com.example.testmid.activities.list_movies_activity.presenter.ListMoviesActivityPresenter
import com.example.testmid.adapters.MovieAdapter
import com.example.testmid.utils.VerticalSpaceItemDecorator
import com.example.testmid.models.Movie
import com.example.testmid.utils.Constants
import com.example.testmid.utils.DeviceUtilis
import com.example.testmid.utils.DialogUtils
import kotlinx.android.synthetic.main.activity_list_movies.*
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Message
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.testmid.models.ApiResponse
import android.support.v4.view.MenuItemCompat.getActionView
import android.support.v7.widget.SearchView
import android.widget.Toast


class ListMoviesActivity : AppCompatActivity(), ViewInterface, WsCallInterface {

    // variable declarations **************************
    private lateinit var presenter: ListMoviesActivityPresenter
    private lateinit var adapter: MovieAdapter
    private lateinit var movies: ArrayList<Movie>
    private var defaultMovies = ArrayList<Movie>()
    private lateinit var drawable: Drawable
    private lateinit var bitmap: Bitmap
    private lateinit var newdrawable: Drawable
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var firstVisibleItemPosition = 0
    private var page = 1
    private var isLoading = false
    private var searchedKeyword = ""

    // ******************************************************
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_movies)
        setSupportActionBar(toolbar)
        drawable = ContextCompat.getDrawable(this, R.drawable.privalia_launcher_round)!!
        bitmap = (drawable as BitmapDrawable).bitmap
        newdrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 50, 50, true))
        getSupportActionBar()!!.setIcon(newdrawable);

        // initialize  the presenter
        presenter = ListMoviesActivityPresenter(this, this, this)


        //  Initialize ItemAnimator, LayoutManager and ItemDecorators
        val layoutManager = LinearLayoutManager(this)
        val itemDecorator = VerticalSpaceItemDecorator(20)
        //  Set the LayoutManager
        recyclerView.setLayoutManager(layoutManager)

        //  Set the ItemDecorator
        recyclerView.addItemDecoration(itemDecorator)
        movies = ArrayList();
        adapter = MovieAdapter(movies, this)
        recyclerView.adapter = adapter
        // call the ws
        presenter.getMovies(page)




        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = layoutManager.childCount
                totalItemCount = layoutManager.itemCount
                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                Log.e("pagination", visibleItemCount.toString() + " " + " " + totalItemCount.toString() + " " + firstVisibleItemPosition.toString())
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && !isLoading) {
                    presenter.getMovies(page)
                    isLoading = true
                }
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val mSearch = menu.findItem(R.id.action_search)
        val mSearchView = mSearch.actionView as SearchView
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.e("serch", newText)
                searchedKeyword = newText
                adapter.search(searchedKeyword, defaultMovies)
                if (adapter.items.size <= 1 && !isLoading) {
                    presenter.getMovies(page)
                    isLoading = true
                }
                return true
            }
        })
        return true
    }


    // ViewInterface  interface  callbacks ************************
    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE

    }

    //********************************************************

    override fun onSuccess(response: ApiResponse?) {
        Log.e("response", response.toString())
        response?.results?.let { defaultMovies.addAll(it) }
        if (!searchedKeyword.isEmpty())
            adapter.search(searchedKeyword, defaultMovies)
        else
            adapter.update(response!!.results)
        page += 1
        isLoading = false

        if (!searchedKeyword.isEmpty())
            if (response?.results?.size == 0 || (visibleItemCount + firstVisibleItemPosition >= totalItemCount && !isLoading) || adapter.items.size <= 1) {
                presenter.getMovies(page)
                isLoading = true
            }

    }

    override fun onFailure(message: String?) {

        if (!DeviceUtilis.isDeviceConnectedToInternet(this)) {
            // show popup
            DialogUtils.showDialog(this, getString(R.string.no_internet_connexion), getString(R.string.ok))
        } else {
            // show popup
            if (message != null)
                DialogUtils.showDialog(this, message, getString(R.string.ok))
            else
                DialogUtils.showDialog(this, getString(R.string.problem_occured), getString(R.string.ok))

        }
    }


}

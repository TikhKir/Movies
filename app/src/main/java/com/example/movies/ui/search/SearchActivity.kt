package com.example.movies.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.utils.datatypes.NetworkState
import com.example.movies.utils.rxutils.Rxview.RxViewUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var viewModel: SearchViewModel
    private var compositeDisposable = CompositeDisposable()
    private val adapter = SearchAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = getString(R.string.search_title)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        initViewModel()
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Int.MAX_VALUE

        initViewListeners()
        return true
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        viewModel.getMoviesLiveData().observe(this, Observer {
            adapter.submitList(it.toList())
        })
        viewModel.getNetworkLiveData().observe(this, Observer {
            when (it) {
                NetworkState.LOADING -> {
                    progress_bar_search.visibility = View.VISIBLE
                }
                NetworkState.CONNECTION_LOST -> {
                    progress_bar_search.visibility = View.GONE
                    txt_error_search.visibility = View.VISIBLE
                    txt_error_search.text = getString(R.string.connection_error)
                }
                NetworkState.LOADED -> {
                    progress_bar_search.visibility = View.GONE
                    txt_error_search.visibility = View.GONE
                }
            }
        })
    }

    private fun initRecyclerView() {
        recyclerViewSearch.layoutManager = LinearLayoutManager(this)
        recyclerViewSearch.adapter = adapter
    }

    private fun initViewListeners() {
        compositeDisposable.add(
            Observable.combineLatest(
                getRawRequest(),
                getRawDate(),
                getRawSafeness(),
                Function3<String, String, Boolean, Triple<String, String, Boolean>>
                { request, date, safeness -> Triple(request, date, safeness) }
            )
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(this, "${it.first} ${it.second} ${it.third}", Toast.LENGTH_SHORT).show()
                    if (it.second != "") viewModel.searchMovie(it.first, it.second.toIntOrNull(), it.third)
                }, {
                    Log.e("RXViewError", it.message )
                })
        )
    }

    private fun getRawDate(): Observable<String> {
        return RxViewUtil.getTextWatcherObservable(editTextSearchYear)
            .map {
                try {
                    if (it.toInt() in 1801..2029) return@map it
                    else return@map ""
                } catch (e: Exception) {
                    return@map ""
                }
            }
    }

    private fun getRawRequest(): Observable<String> {
        return RxViewUtil.getSearchViewTextObservable(searchView)
    }

    private fun getRawSafeness(): Observable<Boolean> {
        return RxViewUtil.getCheckBoxStateObservable(checkBoxAdult)
    }


    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }


}
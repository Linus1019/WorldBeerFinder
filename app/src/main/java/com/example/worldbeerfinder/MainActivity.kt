package com.example.worldbeerfinder

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worldbeerfinder.adapters.BeerListAdapter
import com.example.worldbeerfinder.viewModels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]

        viewModel.beerList.observe(this, Observer {
            if (it == null) return@Observer

            if (beer_list_view.adapter == null) {
                beer_list_view.adapter = BeerListAdapter(this, it)

                (beer_list_view.adapter as BeerListAdapter).itemClickListener =
                    object: BeerListAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            supportFragmentManager.beginTransaction()
                                .add(R.id.fragment_container,
                                    BeerDetailFragment(),
                                    FRAGMENT_TAG
                                )
                                .commit()

                            activity_container.visibility = View.GONE
                            fragment_container.visibility = View.VISIBLE
                            viewModel.selectedBeerItem.value = viewModel.beerList.value!![position]
                        }
                    }
            } else {
                val adapter = (beer_list_view.adapter as BeerListAdapter)
                adapter.beerItems = viewModel.beerList.value!!
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.errorCode.observe(this, Observer { code ->
            when (code) {
                MainViewModel.ErrorCode.NONE -> {
                    error_message.visibility = View.GONE
                    beer_list_view.visibility = View.VISIBLE
                }
                else -> {
                    viewModel.beerList.value = null
                    error_message.visibility = View.VISIBLE
                    beer_list_view.visibility = View.GONE
                    error_message.text = viewModel.errorMessage
                }
            }
        })

        viewModel.keyword.observe(this, Observer {
            beer_list_view.adapter = null
            viewModel.beerList.value = null
        })

        beer_list_view.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == viewModel.beerList.value!!.size - 1) {
                    viewModel.page.value = viewModel.page.value!! + 1
                }
            }
        })

        btn_search.setOnClickListener {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(keyword_text.windowToken, 0)

            viewModel.keyword.value = keyword_text.text.toString()
        }
    }

    override fun onBackPressed() {
        if (fragment_container.visibility == View.VISIBLE) {
            supportFragmentManager.beginTransaction().remove(
                supportFragmentManager
                    .findFragmentByTag(FRAGMENT_TAG) as BeerDetailFragment
            ).commit()

            fragment_container.visibility = View.GONE
            activity_container.visibility = View.VISIBLE

            return
        }

        super.onBackPressed()
    }

    companion object {
        const val FRAGMENT_TAG = "Detail_Fragment"
    }
}

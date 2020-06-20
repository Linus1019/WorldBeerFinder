package com.example.worldbeerfinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

import com.example.worldbeerfinder.viewModels.MainViewModel
import kotlinx.android.synthetic.main.beer_list_item_view.*
import kotlinx.android.synthetic.main.fragment_beer_detail.*

class BeerDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_beer_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(activity!!)[MainViewModel::class.java]

        viewModel.selectedBeerItem.observe(this.viewLifecycleOwner, Observer {
            Glide.with(this).load(it.imageUrl).into(detail_beer_image)
            beer_name_text.text = it.name
            first_brewed_text.text = it.firstBrewed
            tag_line_text.text = it.tagLine
            description_text.text = it.description
        })
    }
}

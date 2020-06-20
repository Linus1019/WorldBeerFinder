package com.example.worldbeerfinder.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.worldbeerfinder.R
import com.example.worldbeerfinder.models.BeerItem
import kotlinx.android.synthetic.main.beer_list_item_view.view.*

class BeerListAdapter(
    val context: Context,
    var beerItems: List<BeerItem?>
) : RecyclerView.Adapter<ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    lateinit var itemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater
            .from(context)
            .inflate(R.layout.beer_list_item_view, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(beerItems[position]?.imageUrl)
            .into(holder.itemView.beer_image)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
        holder.itemView.beer_name.text = beerItems[position]?.name
    }

    override fun getItemCount() = beerItems.size

    private class ItemViewHolder(itemView: View) : ViewHolder(itemView)
}

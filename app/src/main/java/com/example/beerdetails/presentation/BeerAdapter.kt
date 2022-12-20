package com.example.beerdetails.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.beerdetails.R
import com.example.beerdetails.data.BeerEntity
import com.example.beerdetails.data.BeersData
import com.example.beerdetails.databinding.LayoutBeerListChildViewBinding
import com.example.beerdetails.presentation.view.BeerDescriptionScreenActivity

class BeerAdapter(private val context: Context) :
    RecyclerView.Adapter<BeerAdapter.BeerViewHolder>() {

    private var beerList: List<BeersData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val binding = LayoutBeerListChildViewBinding.inflate(
            LayoutInflater.from(context), parent, false
        )

        binding.root.setOnClickListener {
            val beerData = binding.root.getTag(R.id.beer_detail) as BeersData?

            if (beerData != null) {
                BeerDescriptionScreenActivity.startActivity(context, beerData)
            } else {
                Toast.makeText(
                    context, context.getString(R.string.some_error_occurred), Toast.LENGTH_SHORT
                ).show()
            }
        }

        return BeerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        holder.apply {
            val data = beerList[position]

            name.text = data.name
            desc.text = data.description
            Glide.with(context).load(data.imageUrl).placeholder(R.drawable.beer_bottle).into(image)
            root.setTag(R.id.beer_detail, data)
        }
    }

    override fun getItemCount() =beerList.size

    fun setData(beerList: List<BeersData>) {
        this.beerList = beerList
        notifyDataSetChanged()
    }

    class BeerViewHolder(binding: LayoutBeerListChildViewBinding) : ViewHolder(binding.root) {
        val root = binding.root
        val image = binding.ivBeer
        val name = binding.tvName
        val desc = binding.tvTag
    }
}
package com.ridianputra.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ridianputra.githubuserapp.response.ItemsItem
import com.ridianputra.githubuserapp.databinding.ItemRowFnfBinding

class FollowersFollowingAdapter : RecyclerView.Adapter<FollowersFollowingAdapter.ListViewHolder>() {

    private val list = ArrayList<ItemsItem>()

    class ListViewHolder(private var binding: ItemRowFnfBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemsItem){
            binding.apply {
                Glide.with(itemView)
                    .load(item.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(avatar)

                tvItemUsername.text = item.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowFnfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setList(item: ArrayList<ItemsItem>){
        list.clear()
        list.addAll(item)
        notifyDataSetChanged()
    }
}
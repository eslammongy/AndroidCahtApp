package com.eslammongy.chattingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eslammongy.chattingapp.data.model.UserModel
import com.eslammongy.chattingapp.databinding.FriendListItemBinding

class FriendsAdapter(private val userFriendsList: ArrayList<UserModel>):RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FriendListItemBinding.inflate(LayoutInflater.from(parent.context),
            parent , false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val userModel = userFriendsList[position]
        holder.binding.userModel = userModel

    }

    override fun getItemCount(): Int {
        return userFriendsList.size
    }

    inner class ViewHolder(val binding:FriendListItemBinding):RecyclerView.ViewHolder(binding.root)
}
package com.eslammongy.chattingapp.data.model

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

data class UserModel(
    var name: String= "",
    val email: String="",
    val status: String="",
    val image: String="",
    var number: String="",
    val uID: String=""
){

    companion object{
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadingImage(imageView:CircleImageView , imageUrl:String?){
            imageUrl?.let {
              Glide.with(imageView.context).load(imageUrl).into(imageView)
            }
        }
    }
}
package com.wislie.wanandroid.ext

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wislie.wanandroid.R

fun ImageView.loadImage(fragment: Fragment, path:String){
    Glide.with(fragment)
        .load(path)
        .placeholder(R.mipmap.logo)
        .apply(RequestOptions().centerCrop())
        .into(this)
}
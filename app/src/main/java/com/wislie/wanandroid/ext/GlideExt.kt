package com.wislie.wanandroid.ext

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.wislie.wanandroid.R

fun ImageView.loadImage(fragment: Fragment, path:String){
    Glide.with(fragment)
        .load(path)
        .placeholder(R.mipmap.logo)
        .optionalCenterCrop()
        .into(this)
}

fun ImageView.loadCircleImage(fragment: Fragment, path:String){
    Glide.with(fragment)
        .load(path)
        .placeholder(R.mipmap.logo)
        .optionalCircleCrop()
        .into(this)
}
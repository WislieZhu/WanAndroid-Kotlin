package com.wislie.wanandroid.ext

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(path:String){
    Glide.with(this.context)
        .load(this)
        .into(this)
}
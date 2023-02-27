package com.wislie.wanandroid.adapter.holder

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.wislie.common.util.Utils
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.Banner
import com.zhpan.bannerview.BaseViewHolder

class BannerViewHolder(itemView: View) : BaseViewHolder<Banner>(itemView) {

    override fun bindData(data: Banner, position: Int, pageSize: Int) {
        val imageView = itemView.findViewById<ImageView>(R.id.banner_image)
        Glide.with(itemView.context).load(data.imagePath)
            .transform(RoundedCorners(80))
            .into(imageView)
    }
}
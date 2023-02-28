package com.wislie.wanandroid.adapter

import android.os.Bundle
import android.view.View
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.holder.BannerViewHolder
import com.wislie.wanandroid.data.Banner
import com.wislie.wanandroid.util.*
import com.zhpan.bannerview.BaseBannerAdapter

/**
 * banner 分页
 */
class BannerPager : BaseBannerAdapter<Banner, BannerViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_first_page_banner;
    }

    override fun createViewHolder(itemView: View, viewType: Int): BannerViewHolder {
        return BannerViewHolder(itemView)
    }

    override fun onBind(holder: BannerViewHolder, data: Banner, position: Int, pageSize: Int) {
        holder.bindData(data, position, pageSize)
        holder.itemView.setOnClickListener { v ->

            val bundle = Bundle()
            bundle?.run {
                putInt(ARTICLE_TYPE, ArticleType.TYPE_WEBSITE)
                putInt(ARTICLE_ID, data.id )
                putString(ARTICLE_TITLE, data.title)
                putString(ARTICLE_LINK, data.url)
            }

            v.findNav().navigate(R.id.fragment_web, bundle)
        }
    }
}
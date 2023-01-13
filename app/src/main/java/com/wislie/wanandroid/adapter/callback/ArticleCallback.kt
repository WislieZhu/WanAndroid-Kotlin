package com.wislie.wanandroid.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.wislie.wanandroid.data.ArticleInfo

class ArticleCallback: DiffUtil.ItemCallback<ArticleInfo>() {

    override fun areItemsTheSame(oldItem: ArticleInfo, newItem: ArticleInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArticleInfo, newItem: ArticleInfo): Boolean {
        return oldItem == newItem
    }
}
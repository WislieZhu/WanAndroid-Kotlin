package com.wislie.wanandroid.adapter

import com.wislie.common.base.BaseAdapter
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.articleCallback
import com.wislie.wanandroid.adapter.holder.ProjectArticleHolder
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.databinding.ItemProjectArticleBinding

class ProjectArticleAdapter(val collect: (articleInfo: ArticleInfo?) -> Unit) :
    BaseAdapter<ArticleInfo, ItemProjectArticleBinding, ProjectArticleHolder>(articleCallback) {
    override fun onCreateViewHolder(binding: ItemProjectArticleBinding): ProjectArticleHolder {
        return ProjectArticleHolder(binding, collect)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_project_article
    }
}








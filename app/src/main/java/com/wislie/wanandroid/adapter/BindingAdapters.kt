package com.wislie.wanandroid.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.wislie.common.ext.toHtml
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.util.AnimatorUtil
import java.text.SimpleDateFormat

@BindingAdapter("author")
fun bindAuthor(view: TextView, articleInfo: ArticleInfo) { //首页的文章作者
    val author =
        if (TextUtils.isEmpty(articleInfo.shareUser)) articleInfo.author else articleInfo.shareUser
    view.text = author
}

@BindingAdapter("fresh")
fun bindFresh(view: TextView, fresh: Boolean) { //首页的文章是否置顶
    view.visibility = if (fresh) View.VISIBLE else View.GONE
}

@BindingAdapter("titles")
fun bindTitle(view: TextView, title: String) { //首页的文章标题
    view.text = title.toHtml()
}

@BindingAdapter("isCollect")
fun bindCollect(view: ImageView, isCollect: Boolean) { //首页的文章标题
    //view 添加动画
    val tintDrawable: Drawable = DrawableCompat.wrap(view.drawable).mutate()
    if (isCollect) {
        AnimatorUtil.startShake(view)
        DrawableCompat.setTint(tintDrawable, Color.RED)
    } else {
        DrawableCompat.setTint(tintDrawable, Color.GRAY)
    }
    view.setImageDrawable(tintDrawable)
}

@BindingAdapter("filePath")
fun bindFilePath(view: ImageView, filePath: String) {
    Glide.with(view.context)
        .load(filePath)
        .into(view)
}

@BindingAdapter("username")
fun bindUserName(view: TextView, username: String?) {
    view.text = username ?: "请先登录"
}

@BindingAdapter("coinCount")
fun bindCoinCount(view: TextView, coinCount: Int?) {
    view.text = coinCount?.toString() ?: "--"
}

@BindingAdapter("rank")
fun bindRank(view: TextView, rank: String?) {
    view.text = rank ?: "--"
}

@BindingAdapter("myCoinCount")
fun bindMyCoinCount(view: TextView, myCoinCount: Int?) {
    view.text = "+$myCoinCount"
}

@BindingAdapter("myCoinDesc")
fun bindMyCoinDesc(view: TextView, myCoinDesc: String?) {
    myCoinDesc?.run {
        if (this.contains("积分")) {
            view.text = this.subSequence(this.indexOf("积分"), this.length)
        }
    }
}

@BindingAdapter("date")
fun bindDate(view: TextView, date: Long?) {
    date?.run {
        val formatDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        view.text = formatDate.format(date)
    }
}

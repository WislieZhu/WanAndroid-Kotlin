package com.wislie.wanandroid.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wislie.common.ext.toHtml
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.OnEditTextChangeListener
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.data.Tag
import com.wislie.wanandroid.databinding.ItemArticleTagBinding
import com.wislie.wanandroid.util.AnimatorUtil
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import java.text.SimpleDateFormat

@BindingAdapter("author")
fun bindAuthor(view: TextView, articleInfo: ArticleInfo) { //首页的文章作者
    val author =
        if (TextUtils.isEmpty(articleInfo.shareUser)) articleInfo.author else articleInfo.shareUser
    view.text = if(!TextUtils.isEmpty(author)) author else "匿名用户"
}

@BindingAdapter("type")
fun bindType(view: TextView, type: Int?) { //首页的文章是否置顶
    view.visibility = if (type != null && type == 1) View.VISIBLE else View.GONE
}


@BindingAdapter("fresh")
fun bindFresh(view: TextView, fresh: Boolean?) { //是否是新的标签
    view.visibility = if (fresh != null && fresh == true) View.VISIBLE else View.GONE
}

@BindingAdapter("tags")
fun bindTags(view: TagFlowLayout, tags: List<Tag>?) {
    view.adapter = object : TagAdapter<Tag>(tags) {
        override fun getView(parent: FlowLayout, position: Int, t: Tag): View {
            val binding = DataBindingUtil.inflate<ItemArticleTagBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_article_tag,
                parent,
                false
            )
            binding.tag = t
            return binding.root
        }
    }
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

/*<ImageView
app:filePath="@{venue.imageUrl}"
app:error="@{@drawable/venueError}" />*/

@BindingAdapter(value = ["filePath", "error"])
fun bindFilePath(view: ImageView, filePath: String, error: Drawable) {
    Glide.with(view.context)
        .load(filePath)
        .error(error)
        .into(view)
}

@BindingAdapter(value = ["envelopePic"])
fun bindEnvelopePic(view: ImageView, envelopePic: String?) {
    Glide.with(view.context)
        .load(envelopePic)
        .into(view)
}

@BindingAdapter(value = ["parameter"])
fun bindViewVisible(view:View, parameter:String?){
    view.visibility = if (TextUtils.isEmpty(parameter)) View.GONE else View.VISIBLE
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

@BindingAdapter("coinRankIndex")
fun bindCoinRank(view: TextView, coinRankIndex: Int?) {
    view.text = (coinRankIndex ?: 1).toString()
}

@BindingAdapter(value = ["endIconTransformation"])
fun bindEndIconTransformation(textInputLayout: TextInputLayout, accountValue: String?) {
    textInputLayout.isEndIconVisible = !accountValue.isNullOrEmpty()
}

@BindingAdapter(value = ["focusEditText"])
fun TextInputEditText.bindEditTextFocus(
    l: View.OnFocusChangeListener
) {
    onFocusChangeListener = l
}


@BindingAdapter(value = ["editTextChange"])
fun TextInputEditText.bindEditTextChange(l: OnEditTextChangeListener) {
    val textInputEditText = this
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            l.afterTextChanged(textInputEditText, s)
        }
    })
}
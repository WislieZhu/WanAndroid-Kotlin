package com.wislie.wanandroid.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wislie.common.ext.toHtml
import com.wislie.wanandroid.R
import com.wislie.wanandroid.adapter.callback.OnEditTextChangeListener
import com.wislie.wanandroid.data.ArticleInfo
import com.wislie.wanandroid.data.TreeInfo
import com.wislie.wanandroid.data.Tag
import com.wislie.wanandroid.databinding.ItemArticleTagBinding
import com.wislie.wanandroid.databinding.ItemNaviTagBinding
import com.wislie.wanandroid.databinding.ItemSystemTagBinding
import com.wislie.wanandroid.util.AnimatorUtil
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import java.text.SimpleDateFormat

@BindingAdapter("author")
fun bindAuthor(view: TextView, articleInfo: ArticleInfo) { //首页的文章作者
    val author =
        if (TextUtils.isEmpty(articleInfo.shareUser)) articleInfo.author else articleInfo.shareUser
    view.text = if (!TextUtils.isEmpty(author)) author else "匿名用户"
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
    val tempTags = tags?.filter {
        "问答" != it.name
    }
    view.adapter = object : TagAdapter<Tag>(tempTags) {
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
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .into(view)
}

@BindingAdapter(value = ["parameter"])
fun bindViewVisible(view: View, parameter: String?) {
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
fun EditText.bindEditTextChange(l: OnEditTextChangeListener) {
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

@BindingAdapter("systemTags")
fun bindSystemTags(view: TagFlowLayout, tags: List<TreeInfo>?) { //体系
    view.adapter = object : TagAdapter<TreeInfo>(tags) {
        override fun getView(parent: FlowLayout, position: Int, t: TreeInfo): View {
            val binding = DataBindingUtil.inflate<ItemSystemTagBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_system_tag,
                parent,
                false
            )
            binding.systemInfo = t
            return binding.root
        }
    }
}

@BindingAdapter("naviTags")
fun bindNaviTags(view: TagFlowLayout, tags: List<ArticleInfo>?) { //导航
    view.adapter = object : TagAdapter<ArticleInfo>(tags) {
        override fun getView(parent: FlowLayout, position: Int, t: ArticleInfo): View {
            val binding = DataBindingUtil.inflate<ItemNaviTagBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_navi_tag,
                parent,
                false
            )
            binding.articleInfo = t
            return binding.root
        }
    }
}

@BindingAdapter("todoStatus")
fun bindTodoStatus(view: ImageView, status: Int?) {
    status?.run {
        if (status == 0) {
            view.visibility = View.GONE
        } else if (status == 1) {
            view.visibility = View.VISIBLE
        }
    }
}
@BindingAdapter("todoStatusVisible")
fun bindTodoStatusVisible(view: TextView, status: Int?) {
    status?.run {
        if (status == 0) {
            view.visibility = View.VISIBLE
        } else if (status == 1) {
            view.visibility = View.GONE
        }
    }
}


@BindingAdapter("todoStatusIcon")
fun bindTodoStatusIcon(view: ImageView, status: Int?) { //首页的文章标题
    //view 添加动画
    val tintDrawable: Drawable = DrawableCompat.wrap(view.drawable).mutate()
    status?.run {
        if (status == 0) {
            DrawableCompat.setTint(
                tintDrawable,
                ContextCompat.getColor(view.context, R.color.purple_500)
            )
        } else if (status == 1) {
            DrawableCompat.setTint(tintDrawable, Color.RED)
        }
    }
    view.setImageDrawable(tintDrawable)
}

@BindingAdapter(value = ["checkedChange"])
fun RadioGroup.bindCheckedChange(l: RadioGroup.OnCheckedChangeListener) {
    setOnCheckedChangeListener(l)
}

@BindingAdapter(value = ["todoType"])
fun bindRadioButtonType(radioGroup: RadioGroup, type: Int?) {
    type?.run {
        if (this == 1) {
            radioGroup.findViewById<RadioButton>(R.id.rb_job).isChecked = true
            radioGroup.findViewById<RadioButton>(R.id.rb_life).isChecked = false
            radioGroup.findViewById<RadioButton>(R.id.rb_entertainment).isChecked = false
        } else if (this == 2) {
            radioGroup.findViewById<RadioButton>(R.id.rb_job).isChecked = false
            radioGroup.findViewById<RadioButton>(R.id.rb_life).isChecked = true
            radioGroup.findViewById<RadioButton>(R.id.rb_entertainment).isChecked = false
        } else if (this == 3) {
            radioGroup.findViewById<RadioButton>(R.id.rb_job).isChecked = false
            radioGroup.findViewById<RadioButton>(R.id.rb_life).isChecked = false
            radioGroup.findViewById<RadioButton>(R.id.rb_entertainment).isChecked = true
        }
    }
}

@BindingAdapter(value = ["todoPriority"])
fun bindRadioButtonPriority(radioGroup: RadioGroup, priority: Int?) {
    priority?.run {
        if (this == 1) {
            radioGroup.findViewById<RadioButton>(R.id.rb_import).isChecked = true
            radioGroup.findViewById<RadioButton>(R.id.rb_generate).isChecked = false
        } else if (this == 2) {
            radioGroup.findViewById<RadioButton>(R.id.rb_import).isChecked = false
            radioGroup.findViewById<RadioButton>(R.id.rb_generate).isChecked = true
        }
    }
}
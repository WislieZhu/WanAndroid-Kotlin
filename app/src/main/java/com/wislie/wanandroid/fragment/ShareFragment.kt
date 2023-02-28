package com.wislie.wanandroid.fragment

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentShareBinding
import com.wislie.common.ext.showToast
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.viewmodel.ArticlesViewModel
import com.wislie.wanandroid.viewmodel.ShareStateViewModel

/**
 * 分享文章
 */
class ShareFragment : BaseViewModelFragment<ShareStateViewModel, FragmentShareBinding>() {

    private val articlesViewModel: ArticlesViewModel by viewModels()

    override fun init(root: View) {
        super.init(root)
        binding.shareStateVm = mViewModel
        binding.tb.toolbar.run {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            title = "分享文章"
            setTitleTextColor(ContextCompat.getColor(hostActivity, R.color.white))
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }
        binding.tvShareAuthor.text = Settings.nickname
        binding.btnCommit.setOnClickListener {
            when {
                mViewModel?.title?.get().isNullOrEmpty() ->
                    hostActivity.showToast("请输入标题")
                mViewModel?.link?.get().isNullOrEmpty() ->
                    hostActivity.showToast("请输入链接")
                else -> {
                    articlesViewModel.shareArticle(
                        mViewModel?.title?.get()!!,
                        mViewModel?.link?.get()!!
                    )
                }
            }
        }
    }

    override fun observeData() {
        super.observeData()
        articlesViewModel.shareArticleLiveData.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                App.instance().appViewModel.shareAddLiveData.value = Any()
                findNav().navigateUp()
            })
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_share
    }
}
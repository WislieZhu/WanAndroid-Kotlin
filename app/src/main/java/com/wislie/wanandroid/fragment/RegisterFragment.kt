package com.wislie.wanandroid.fragment

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.findNav
import com.wislie.common.ext.showToast
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentRegisterBinding
import com.wislie.wanandroid.ext.*
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.viewmodel.LoginStateViewModel
import com.wislie.wanandroid.viewmodel.LoginViewModel

class RegisterFragment : BaseViewModelFragment<LoginStateViewModel, FragmentRegisterBinding>() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun init(root: View) {
        super.init(root)
        binding.loginStateVm = mViewModel
        binding.tb.toolbar.run {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            setTitleTextColor(Color.WHITE)
            title = "注册"
            setTitleTextColor(ContextCompat.getColor(hostActivity, R.color.white))
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }

        binding.inputLayoutAccount.clearEditText()
        binding.inputLayoutPwd.setPasswordTransformation()

        binding.btnRegister.setOnClickListener {
            when {
                mViewModel?.account?.get()?.isEmpty() == true ->
                    hostActivity.showToast("请输入账号")
                mViewModel?.password?.get()?.isEmpty() == true ->
                    hostActivity.showToast("请输入密码")
                mViewModel?.confirmPassword?.get()?.isEmpty() == true ->
                    hostActivity.showToast("请输入确认密码")
                mViewModel?.password?.get()?.length ?: 0 < 6 ->
                    hostActivity.showToast("密码最少6位")
                mViewModel?.password?.get() == mViewModel?.confirmPassword?.get() ->
                    hostActivity.showToast("两次输入的密码不一致")
                else -> loginViewModel.register(
                    mViewModel?.account?.get()!!,
                    mViewModel?.password?.get()!!,
                    mViewModel?.password?.get()!!
                )
            }
        }
    }

    override fun observeData() {
        super.observeData()
        loginViewModel.registerResultLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { userInfo ->
                    userInfo?.run {
                        App.instance().appViewModel.userInfoLiveData.value = this
                        Settings.nickname = this.nickname
                    }
                    Settings.logined = true
                    findNav().popBackStack(R.id.fragment_main, false)
                })
            }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_register
    }
}



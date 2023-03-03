package com.wislie.wanandroid.fragment

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.findNav
import com.wislie.common.ext.showToast
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentLoginBinding
import com.wislie.wanandroid.ext.*
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.viewmodel.LoginStateViewModel
import com.wislie.wanandroid.viewmodel.LoginViewModel

class LoginFragment : BaseViewModelFragment<LoginStateViewModel, FragmentLoginBinding>() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun init(root: View) {
        super.init(root)
        binding.loginStateVm = mViewModel
        binding.tb.toolbar.run {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            setTitleTextColor(Color.WHITE)
            title = "登录"
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }

        binding.inputLayoutAccount.clearEditText()
        binding.inputLayoutPwd.setPasswordTransformation()

        binding.btnLogin.setOnClickListener { //登录
            when {
                mViewModel?.account?.get()?.isEmpty() == true ->
                    hostActivity.showToast("请输入账号")
                mViewModel?.password?.get()?.isEmpty() == true ->
                    hostActivity.showToast("请输入密码")
                mViewModel?.password?.get()?.length ?: 0 < 6 ->
                    hostActivity.showToast("密码最少6位")
                else -> loginViewModel.login(
                    mViewModel?.account?.get()!!,
                    mViewModel?.password?.get()!!
                )
            }
        }
        binding.tvRegister.setOnClickListener { //注册页面
            val direction =
                LoginFragmentDirections.actionFragmentLoginToFragmentRegister()
            findNav().navigate(direction)
        }

    }

    override fun observeData() {
        super.observeData()
        loginViewModel.loginInfoResultLiveData
            .observe(viewLifecycleOwner) { resultState ->
                Log.i("wislieZhu","11111111111111")
                parseState(resultState, { userInfo ->
                    Log.i("wislieZhu","登录的用户信息:$userInfo")
                    userInfo?.run {
                        App.instance().appViewModel.userInfoLiveData.value = this
                        Settings.nickname = this.nickname
                        Settings.logined = true
                    }
                    findNav().navigateUp()
                })
            }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_login
    }

}



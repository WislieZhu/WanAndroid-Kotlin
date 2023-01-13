package com.wislie.wanandroid.fragment

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentLoginBinding
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.viewmodel.LoginViewModel

class LoginFragment : BaseViewModelFragment<BaseViewModel, FragmentLoginBinding>() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun init(root: View) {
        super.init(root)
        binding.btnLogin.setOnClickListener {
            val userName = binding.etUsername.text.toString()
            val password = binding.etPwd.text.toString()
            loginViewModel.login(userName, password)
        }
    }

    override fun observeData() {
        super.observeData()
        loginViewModel.loginInfoResultLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState,{ userInfo ->
                    userInfo?.run {
                        App.instance().appViewModel.userInfoLiveData.value = this
                    }
                    Settings.isLogined = true
                    findNav().navigateUp()
                })
            }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_login
    }

    override fun loadData() {
    }
}

//snack 想想看怎么添加进来

//run  let  also apply with 的性能对比

package com.wislie.wanandroid.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Editable
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.wislie.common.base.BaseViewModel
import com.wislie.common.base.BaseViewModelFragment
import com.wislie.common.base.parseState
import com.wislie.common.ext.findNav
import com.wislie.wanandroid.App
import com.wislie.wanandroid.R
import com.wislie.wanandroid.databinding.FragmentLoginBinding
import com.wislie.wanandroid.ext.*
import com.wislie.wanandroid.util.Settings
import com.wislie.wanandroid.viewmodel.LoginStateViewModel
import com.wislie.wanandroid.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.include_toolbar.*

class LoginFragment : BaseViewModelFragment<LoginStateViewModel, FragmentLoginBinding>() {

    private val loginViewModel: LoginViewModel by viewModels()

    @SuppressLint("RestrictedApi")
    override fun init(root: View) {
        super.init(root)
        with(toolbar) {
            setNavigationIcon(R.mipmap.ic_back)
            setBackgroundColor(ContextCompat.getColor(hostActivity, R.color.purple_500))
            setTitleTextColor(Color.WHITE)
            title = "登录"
            setNavigationOnClickListener {
                findNav().navigateUp()
            }
        }

        binding.loginStateVm = mViewModel

        /*binding.inputLayoutAccount.setEndIconTransformation{editText: EditText? ->
            editText.len()
        }*/
        binding.inputLayoutAccount.clearEditText()

       /* binding.etAccount.addFocusListener { hasFocus ->
            binding.inputLayoutAccount.setStartIconColor(hasFocus)
        }*/

        binding.etAccount.addTextListener(etAfterTextChanged = { editable: Editable? ->
            editable?.run {
                binding.inputLayoutAccount.error = if (this.length > 10) {
                    "账号长度超出限制"
                } else {
                    null
                }

                mViewModel?.account?.set(this.trim().toString())

                /*binding.inputLayoutAccount.setEndIconTransformation{editText: EditText? ->
                    editText.len()
                }*/
            }
        })

        binding.inputLayoutPwd.setPasswordTransformation()
        binding.inputLayoutPwd.setEndIconTransformation { editText: EditText? ->
            editText.len()
        }

        binding.etPassword.addFocusListener { hasFocus ->
            binding.inputLayoutPwd.setStartIconColor(hasFocus)
        }

        binding.etPassword.addTextListener(etAfterTextChanged = { editable: Editable? ->
            editable?.run {
                binding.inputLayoutPwd.setEndIconTransformation { editText: EditText? ->
                    editText.len()
                }
            }
        })



        binding.btnLogin.setOnClickListener {

            /* val userName = binding.etUsername.text.toString()
             val password = binding.etPwd.text.toString()
             loginViewModel.login(userName, password)*/
        }

    }

    override fun observeData() {
        super.observeData()
        loginViewModel.loginInfoResultLiveData
            .observe(viewLifecycleOwner) { resultState ->
                parseState(resultState, { userInfo ->
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

    inner class ProxyClick{

        fun focusAccountEditText(hasFocus:Boolean){
            binding.inputLayoutAccount.setStartIconColor(hasFocus)
        }
    }
}



package com.wislie.common.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.wislie.common.ext.dismissLoading
import com.wislie.common.ext.showLoading
import com.wislie.common.util.noleakdialog.NoLeakNiceDialog


abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    lateinit var hostActivity: AppCompatActivity
    lateinit var binding: VB

    var loadingDialog: NoLeakNiceDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hostActivity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = if (isBindingLayout()) {
            binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.root
        } else {
            inflater.inflate(getLayoutResId(), container, false)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    abstract fun getLayoutResId(): Int

    abstract fun init(root: View)

    open fun isBindingLayout() = true


}

fun <R> BaseFragment<*>.parseStateNoLogin(
    resultState: ResultState<R>,
    success: (R) -> Unit
) {
    when (resultState) {
        is ResultState.Success -> {
            success.invoke(resultState.data)
        }
        is ResultState.Error -> {
            Toast.makeText(hostActivity, "${resultState.exception.message}", Toast.LENGTH_SHORT)
                .show()
        }
        is ResultState.Loading -> {
            if (resultState.isShownDialog) {
                showLoading(resultState.loadingMessage)
            } else {
                dismissLoading()
            }
        }
    }
}


fun <R> BaseFragment<*>.parseState(
    resultState: ResultState<R>,
    success: (R) -> Unit,
    login: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Success -> {
            success.invoke(resultState.data)
        }
        is ResultState.Error -> {
            if (resultState.errorCode == -1001) {
                login?.invoke()
            }
            Toast.makeText(hostActivity, "${resultState.exception.message}", Toast.LENGTH_SHORT)
                .show()
        }
        is ResultState.Loading -> {
            if (resultState.isShownDialog) {
                showLoading(resultState.loadingMessage)
            } else {
                dismissLoading()
            }
        }
    }
}

fun <R> BaseFragment<*>.parseListState(
    resultState: ResultState<R>,
    success: (R, Int) -> Unit,
    login: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Success -> {
            success.invoke(resultState.data, resultState.position)
        }
        is ResultState.Error -> {
            Toast.makeText(hostActivity, "${resultState.exception.message}", Toast.LENGTH_SHORT)
                .show()
            if (resultState.errorCode == -1001) {
                login?.invoke()
            }
        }
        is ResultState.Loading -> {
            if (resultState.isShownDialog) {
                showLoading(resultState.loadingMessage)
            } else {
                dismissLoading()
            }
        }
    }
}

val BaseFragment<*>.TAG: String get() = this::class.java.simpleName

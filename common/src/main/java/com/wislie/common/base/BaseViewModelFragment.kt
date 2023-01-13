package com.wislie.common.base

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import java.lang.reflect.ParameterizedType

abstract class BaseViewModelFragment<VM : BaseViewModel, VB : ViewDataBinding> :
    BaseLazyFragment<VB>() {

    protected lateinit var mBaseLoadService: LoadService<*>

    val mViewModel: VM? by lazy {
        var vm: VM? = null
        val superclass = javaClass.genericSuperclass
        if (superclass is ParameterizedType) {

            val type = superclass.actualTypeArguments[0]
            if (type is Class<*>) {
                vm = ViewModelProvider(this)[type as Class<VM>]
            }

        }
        vm
    }

    fun registerLoadSir(view: View, block: (view: View) -> Unit) {
        mBaseLoadService = LoadSir.getDefault().register(view) { v -> block(v) }
    }

    override fun init(root: View) {
        observeData()
    }

    open fun observeData(){}
}
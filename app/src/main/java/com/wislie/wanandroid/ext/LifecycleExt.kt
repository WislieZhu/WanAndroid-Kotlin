package com.wislie.wanandroid.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.flowWithLifecycle2(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.(T) -> Unit,
) = lifecycleOwner.lifecycleScope.launch {
    //前后台切换时可以重复订阅数据。如：Lifecycle.State是STARTED，那么在生命周期进入 STARTED 状态时开始任务，在 STOPED 状态时停止订阅
    flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collect { block(it) }
}

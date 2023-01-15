package com.wislie.common.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class BaseAdapter<T : Any, Binding : ViewDataBinding, VH : BaseVHolder<T>>(itemCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, VH>(itemCallback) {

    /**
     * 刷新状态监听
     */
    private lateinit var mOnRefreshStateListener: (State) -> Unit

    /**
     * 向后加载更多状态监听
     */
    private lateinit var mOnLoadMoreStateListener: (State) -> Unit

    /**
     * 向前加载更多监听
     */
    private lateinit var mOnPrependStateListener: (State) -> Unit

    /*init {
        addLoadStateListener {



            if (::mOnRefreshStateListener.isInitialized) {
                dispatchState(
                    it.refresh, //在初始化刷新的使用
                    it.source.append.endOfPaginationReached,
                    mOnRefreshStateListener
                )
            }
            if (::mOnLoadMoreStateListener.isInitialized) {
                dispatchState(
                    it.append, //在加载更多的时候使用
                    it.source.append.endOfPaginationReached,
                    mOnLoadMoreStateListener
                )
            }
             if (::mOnPrependStateListener.isInitialized) {
                 dispatchState(
                     it.prepend, //在当前列表头部添加数据的时候使用
                     it.source.append.endOfPaginationReached,
                     mOnPrependStateListener
                 )
             }
        }


    }*/


    private fun dispatchState(
        loadState: LoadState,
        noMoreData: Boolean,
        stateListener: (State) -> Unit
    ) {
        when (loadState) {
            is LoadState.Loading -> {
                stateListener(State.Loading)
            }
            is LoadState.NotLoading -> {
                stateListener(State.Success(noMoreData))
            }
            is LoadState.Error -> {
                stateListener(State.Error)
            }
        }
    }

    /**
     * 刷新状态监听
     */
    fun setOnRefreshStateListener(listener: (State) -> Unit) {
        mOnRefreshStateListener = listener
    }

    /**
     * 向后加载更多状态监听
     */
    fun setOnLoadMoreStateListener(listener: (State) -> Unit) {
        mOnLoadMoreStateListener = listener
    }

    /**
     * 向前加载更多状态监听
     */
    fun setOnPrependStateListener(listener: (State) -> Unit) {
        mOnPrependStateListener = listener
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding =
            DataBindingUtil.inflate<Binding>(
                LayoutInflater.from(parent.context),
                getItemLayoutId(), parent, false
            )
        return onCreateViewHolder(binding)
    }

    abstract fun getItemLayoutId(): Int

    abstract fun onCreateViewHolder(binding: Binding): VH

}
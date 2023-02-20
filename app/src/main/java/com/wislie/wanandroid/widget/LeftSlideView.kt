package com.wislie.wanandroid.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.TextView
import com.wislie.wanandroid.R

class LeftSlideView : HorizontalScrollView {

    private var once = false//在onMeasure中只执行一次的判断
    private lateinit var mDeleteTv: TextView//删除按钮
    private var mScrollWidth: Int = 0//记录滚动条可以滚动的距离
    private lateinit var mIonSlidingButtonListener: IonSlidingButtonListener;//自定义的接口，用于传达滑动事件等
    private var isOpen = false//记录按钮菜单是否打开，默认关闭false


    constructor(context: Context) : super(context, null)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        this.overScrollMode = OVER_SCROLL_NEVER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!once) {
            mDeleteTv = findViewById(R.id.tv_delete)
            once = true;
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            this.scrollTo(0, 0)
            //获取水平滚动条可以滑动的范围，即右侧“设置”、“删除”按钮的总宽度
            mScrollWidth = mDeleteTv.width
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        super.onTouchEvent(ev)
        when (ev?.action) {
            MotionEvent.ACTION_DOWN or MotionEvent.ACTION_MOVE -> {
                mIonSlidingButtonListener.onDownOrMove(this)
            }
            MotionEvent.ACTION_UP or MotionEvent.ACTION_CANCEL -> {
                changeScrollx()
                return true
            }
        }
        return super.onTouchEvent(ev)


    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        //改变view的在x轴方向的位置
        mDeleteTv.translationX = 1f
    }

    /**
     * 6.按滚动条被拖动距离判断关闭或打开菜单
     * getScrollX()                view的左上角相对于母视图的左上角的X轴偏移量
     * smoothScrollTo(x, y);        参数：相对于ScrollView左上角的位置来说，你要移动的位置
     */
    fun changeScrollx() {
        if (scrollX >= (mScrollWidth / 2)) {
            this.smoothScrollTo(mScrollWidth, 0)
            isOpen = true
            mIonSlidingButtonListener.onMenuIsOpen(this)
        } else {
            this.smoothScrollTo(0, 0)
            isOpen = false
        }
    }

    /**
     * 7.打开菜单
     */
    fun openMenu() {
        if (isOpen) {
            return
        }
        this.smoothScrollTo(mScrollWidth, 0)//相对于原来没有滑动的位置x轴方向偏移了mScrollWidth，y轴方向没有变化。
        isOpen = true
        mIonSlidingButtonListener.onMenuIsOpen(this)
    }

    /**
     * 8.关闭菜单
     */
    fun closeMenu() {
        if (!isOpen) {
            return
        }
        this.smoothScrollTo(0, 0)//相对于原来没有滑动的位置,x轴方向、y轴方向都没有变化，即回到原来的位置了。
        isOpen = false
    }


    /**
     * 9.接口定义及注册方法
     */
    fun setSlidingButtonListener(listener: IonSlidingButtonListener) {
        mIonSlidingButtonListener = listener
    }

    interface IonSlidingButtonListener {

        //该方法在Adapter中实现
        fun onMenuIsOpen(view: View)//判断菜单是否打开
        fun onDownOrMove(leftSlideView: LeftSlideView)//滑动或者点击了Item监听
    }


}
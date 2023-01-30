package com.wislie.wanandroid.ext

import android.content.Context
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.wislie.common.util.Utils
import com.wislie.wanandroid.R
import com.wislie.wanandroid.data.ProjectCategory
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView


fun CommonNavigator.setNavigator(
    indicator: MagicIndicator,
    viewPager: ViewPager2,
    tabNameList: List<String>
) {


    adapter = object : CommonNavigatorAdapter() {
        override fun getCount(): Int {
            return tabNameList.size
        }

        override fun getTitleView(context: Context, index: Int): IPagerTitleView? {
            val scaleTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
            //设置文本
            scaleTransitionPagerTitleView.text = tabNameList[index]
            //字体大小
            scaleTransitionPagerTitleView.textSize = 16f
            //未选中颜色
            scaleTransitionPagerTitleView.normalColor =
                ContextCompat.getColor(context, R.color.white)
            //选中颜色
            scaleTransitionPagerTitleView.selectedColor =
                ContextCompat.getColor(context, R.color.white)
            //点击事件
            scaleTransitionPagerTitleView.setOnClickListener { view: View? ->
                viewPager.currentItem = index
            }
            return scaleTransitionPagerTitleView
        }

        override fun getIndicator(context: Context): IPagerIndicator? {
            val linePagerIndicator = LinePagerIndicator(context)
            linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
            //线条的宽高度
            linePagerIndicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
            linePagerIndicator.lineWidth = UIUtil.dip2px(context, 30.0).toFloat()
            //线条的圆角
            linePagerIndicator.roundRadius = UIUtil.dip2px(context, 6.0).toFloat()
            linePagerIndicator.startInterpolator = AccelerateInterpolator()
            linePagerIndicator.endInterpolator = DecelerateInterpolator(2.0f)
            //线条的颜色
            linePagerIndicator.setColors(ContextCompat.getColor(context, R.color.white))
            return linePagerIndicator
        }
    }


    indicator.navigator = this
    viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            indicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            indicator.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            indicator.onPageScrollStateChanged(state)
        }
    })
}
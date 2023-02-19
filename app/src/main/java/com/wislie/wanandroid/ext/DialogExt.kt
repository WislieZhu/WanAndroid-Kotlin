package com.wislie.wanandroid.ext

import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.Calendar.Scheme
import com.haibin.calendarview.CalendarLayout
import com.haibin.calendarview.CalendarView
import com.haibin.calendarview.CalendarView.OnCalendarSelectListener
import com.haibin.calendarview.CalendarView.OnYearChangeListener
import com.shehuan.nicedialog.ViewHolder
import com.wislie.common.base.BaseFragment
import com.wislie.common.util.noleakdialog.BNiceDialog
import com.wislie.common.util.noleakdialog.NoLeakNiceDialog
import com.wislie.common.util.noleakdialog.NoLeakViewConvertListener
import com.wislie.wanandroid.R

/**
 *    author : Wislie
 *    e-mail : 254457234@qq.comn
 *    date   : 2023/1/24 8:51 PM
 *    desc   : 对话框
 *    version: 1.0
 */

/**
 * 清理搜索历史
 */
fun BaseFragment<*>.clearSearchHistory(confirm:()->Unit){
    val frag = this
    activity?.let {
        if (!it.isFinishing) {
            if (loadingDialog === null) {
                loadingDialog = NoLeakNiceDialog.init()
            }
            loadingDialog?.run {
                if (!this.isAdded) {
                    setLayout(R.layout.layout_clear_search_history)
                    setGravity(Gravity.CENTER)
                    setMargin(30)
                    setOutCancel(true)
                    setConvertListener(object : NoLeakViewConvertListener() {
                        override fun convertView(holder: ViewHolder?, dialog: BNiceDialog?) {
                            holder?.run {
                                this.setOnClickListener(R.id.tv_clear_confirm, object : View.OnClickListener{
                                    override fun onClick(v: View?) {
                                        confirm()
                                        dialog?.run {
                                            dismiss()
                                        }
                                    }
                                })
                                this.setOnClickListener(R.id.tv_clear_cancel, object : View.OnClickListener{
                                    override fun onClick(v: View?) {
                                        dialog?.run {
                                            dismiss()
                                        }
                                    }
                                })
                            }
                        }
                    })
                    loadingDialog?.show(it.supportFragmentManager, frag.javaClass.simpleName)
                }
            }
        }
    }
}

fun BaseFragment<*>.showCalendar(){
    val frag = this
    activity?.let {
        if (!it.isFinishing) {
            if (loadingDialog === null) {
                loadingDialog = NoLeakNiceDialog.init()
            }
            loadingDialog?.run {
                if (!this.isAdded) {
                    setLayout(R.layout.dialog_choose_calendar)
                    setGravity(Gravity.CENTER)
                    setMargin(15)
                    setOutCancel(true)
                    setConvertListener(object : NoLeakViewConvertListener() {
                        override fun convertView(holder: ViewHolder?, dialog: BNiceDialog?) {
                            holder?.run {
                                val mTextMonthDay =holder.getView<TextView>(R.id.tv_month_day)
                                val mTextYear = holder.getView<TextView>(R.id.tv_year)
                                val mTextLunar = holder.getView<TextView>(R.id.tv_lunar)
                                val mCalendarLayout = holder.getView<CalendarLayout>(R.id.calendarLayout)
                                val mCalendarView = holder.getView<CalendarView>(R.id.calendarView)
                                val  mTextCurrentDay = holder.getView<TextView>(R.id.tv_current_day)
                                mTextYear.text = mCalendarView.curYear.toString()
                                var mYear = mCalendarView.curYear
                                mTextMonthDay.text = mCalendarView.curMonth
                                    .toString() + "月" + mCalendarView.curDay + "日"
                                mTextLunar.text = "今日"
                                mTextCurrentDay.text = mCalendarView.curDay.toString()
                                mTextMonthDay.setOnClickListener(View.OnClickListener {
                                    if (!mCalendarLayout.isExpand) {
                                        mCalendarLayout.expand()
                                        return@OnClickListener
                                    }
                                })
                                holder.setOnClickListener(R.id.fl_current,
                                    View.OnClickListener {
                                        mCalendarView.scrollToCurrent()
                                    })

                                mCalendarView.setOnCalendarSelectListener(object :OnCalendarSelectListener{
                                    override fun onCalendarOutOfRange(calendar: Calendar?) {

                                    }

                                    override fun onCalendarSelect(
                                        calendar: Calendar,
                                        isClick: Boolean
                                    ) {
                                        mTextLunar.visibility = View.VISIBLE
                                        mTextYear.visibility = View.VISIBLE
                                        mTextMonthDay.text =
                                            calendar.month.toString() + "月" + calendar.day + "日"
                                        mTextYear.text = calendar.year.toString()
                                        mTextLunar.text = calendar.lunar
                                        mYear = calendar.year

                                        Log.e(
                                            "onDateSelected", "  -- " + calendar.year +
                                                    "  --  " + calendar.month +
                                                    "  -- " + calendar.day +
                                                    "  --  " + isClick + "  --   " + calendar.scheme
                                        )
                                    }
                                })
                                mCalendarView.setOnYearChangeListener(object :OnYearChangeListener{
                                    override fun onYearChange(year: Int) {
                                        mTextMonthDay.text = year.toString()
                                    }

                                })
                                mCalendarView.setOnClickCalendarPaddingListener(object :CalendarView.OnClickCalendarPaddingListener{
                                    override fun onClickCalendarPadding(
                                        x: Float,
                                        y: Float,
                                        isMonthView: Boolean,
                                        adjacentCalendar: Calendar?,
                                        obj: Any?
                                    ) {
                                        Log.e(
                                            "onClickCalendarPadding",
                                            "  --  $x  $y  $obj  $adjacentCalendar"
                                        )
                                        Toast.makeText(
                                            hostActivity,
                                            adjacentCalendar?.year.toString() + "年，第" + obj + "周",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                            }
                        }
                    })
                    loadingDialog?.show(it.supportFragmentManager, frag.javaClass.simpleName)
                }
            }
        }
    }
}
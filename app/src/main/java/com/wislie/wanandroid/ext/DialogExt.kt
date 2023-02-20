package com.wislie.wanandroid.ext

import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.haibin.calendarview.CalendarView.OnCalendarSelectListener
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
inline fun BaseFragment<*>.clearSearchHistory(crossinline confirm: () -> Unit) {
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
                                this.setOnClickListener(
                                    R.id.tv_clear_confirm
                                ) {
                                    confirm()
                                    dialog?.run {
                                        dismiss()
                                    }
                                }
                                this.setOnClickListener(
                                    R.id.tv_clear_cancel
                                ) {
                                    dialog?.run {
                                        dismiss()
                                    }
                                }
                            }
                        }
                    })
                    loadingDialog?.show(it.supportFragmentManager, frag.javaClass.simpleName)
                }
            }
        }
    }
}

inline fun BaseFragment<*>.showCalendar(crossinline confirm: (Int, Int, Int) -> Unit) {
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
                                val textMonthDay = holder.getView<TextView>(R.id.tv_month_day)
                                val textYear = holder.getView<TextView>(R.id.tv_year)
                                val textLunar = holder.getView<TextView>(R.id.tv_lunar)
                                val calendarView = holder.getView<CalendarView>(R.id.calendarView)
                                val textCurrentDay = holder.getView<TextView>(R.id.tv_current_day)
                                textYear.text = calendarView.curYear.toString()
                                var year = calendarView.curYear
                                var month = calendarView.curMonth
                                var day = calendarView.curDay
                                textMonthDay.text = calendarView.curMonth
                                    .toString() + "月" + calendarView.curDay + "日"
                                textLunar.text = "今日"
                                textCurrentDay.text = calendarView.curDay.toString()

                                holder.setOnClickListener(R.id.fl_current,
                                    View.OnClickListener {
                                        calendarView.scrollToCurrent()
                                    })

                                calendarView.setOnCalendarSelectListener(object :
                                    OnCalendarSelectListener {
                                    override fun onCalendarOutOfRange(calendar: Calendar?) {

                                    }

                                    override fun onCalendarSelect(
                                        calendar: Calendar,
                                        isClick: Boolean
                                    ) {
                                        textLunar.visibility = View.VISIBLE
                                        textYear.visibility = View.VISIBLE
                                        textMonthDay.text =
                                            calendar.month.toString() + "月" + calendar.day + "日"
                                        textYear.text = calendar.year.toString()
                                        textLunar.text = calendar.lunar
                                        year = calendar.year
                                        month = calendar.month
                                        day = calendar.day
                                    }
                                })
                                calendarView.setOnYearChangeListener { year ->
                                    textMonthDay.text = year.toString()
                                }

                                holder.setOnClickListener(R.id.tv_confirm) { //确定
                                    confirm.invoke(year, month, day)
                                    dialog?.dismiss()
                                }
                                holder.setOnClickListener(R.id.tv_cancel) { //取消
                                    dialog?.dismiss()
                                }
                            }
                        }
                    })
                    loadingDialog?.show(it.supportFragmentManager, frag.javaClass.simpleName)
                }
            }
        }
    }
}
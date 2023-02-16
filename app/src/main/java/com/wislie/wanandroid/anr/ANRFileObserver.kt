package com.wislie.wanandroid.anr

import android.os.Build
import android.os.FileObserver
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File

@RequiresApi(Build.VERSION_CODES.Q)
class ANRFileObserver(file: File) : FileObserver(file) {

    override fun onEvent(event: Int, path: String?) {
        when (event) {
            ACCESS -> // 文件被访问
                Log.i(TAG, "ACCESS = $path");
            ATTRIB -> // 文件属性被修改，如 chmod、chown、touch 等
                Log.i(TAG, "ATTRIB = $path");
            CLOSE_NOWRITE -> // 不可写文件被 close
                Log.i(TAG, "CLOSE_NOWRITE = $path");
            CREATE -> // 创建新文件
                Log.i(TAG, "CREATE = $path");
            DELETE -> // 文件被删除
                Log.i(TAG, "DELETE = $path");
            DELETE_SELF -> // 自删除，即一个可执行文件在执行时删除自己
                Log.i(TAG, "DELETE_SELF = $path");
            MODIFY -> // 文件被修改
                Log.i(TAG, "MODIFY = $path");
            MOVE_SELF -> // 自移动，即一个可执行文件在执行时移动自己
                Log.i(TAG, "MOVE_SELF = $path");
            MOVED_FROM -> // 文件被移走
                Log.i(TAG, "MOVED_FROM = $path");
            MOVED_TO -> // 文件被移动过来
                Log.i(TAG, "MOVED_TO = $path");
            OPEN -> // 文件被打开
                Log.i(TAG, "OPEN = $path");
            else ->
                Log.i(TAG, "event = $event, path = $path");

        }
    }

    companion object {
        const val TAG = "ANRFileObserver"
    }
}
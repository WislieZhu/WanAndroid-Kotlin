package com.wislie.wanandroid.ext

import android.content.Context
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions

/**
 * 请求权限
 */
fun requestPermission(context: Context, vararg permissions: String, grantedAction: () -> Unit) {

    XXPermissions
        .with(context)
        .permission(permissions)
        .request(object : OnPermissionCallback {
            override fun onGranted(
                granted: List<String>,
                all: Boolean
            ) {
                if (all) {
                    grantedAction.invoke()
                }
            }

            override fun onDenied(
                denied: List<String>,
                never: Boolean
            ) {
                if (never) {
                    context.showToast("拍照权限被永久拒绝授权，请手动授予拍照权限")
                    XXPermissions.startPermissionActivity(
                        context,
                        denied
                    )
                } else {
                    context.showToast("获取拍照权限失败")
                }
            }
        })
}
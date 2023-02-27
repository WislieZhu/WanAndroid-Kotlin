package com.wislie.common.asm;

import android.view.View;

import com.wislie.common.util.NetworkUtil;

public class CheckNetwork {

    public static boolean isAvailable(View view){
       return NetworkUtil.isNetworkAvailable(view.getContext());
    }
}

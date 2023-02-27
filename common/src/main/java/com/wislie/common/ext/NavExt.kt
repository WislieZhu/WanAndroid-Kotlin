package com.wislie.common.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wislie.common.navigation.NavHostFragment

fun Fragment.findNav(): NavController =
    NavHostFragment.findNavController(this)

fun View.findNav():NavController =
    Navigation.findNavController(this)


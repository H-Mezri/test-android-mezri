package com.mezri.bigburger.ui

import android.view.View
import androidx.fragment.app.Fragment
import com.mezri.bigburger.data.room.AppDatabase

interface MainActivityInterface {
    fun showFragment(fragment: Fragment, view: View?)
}
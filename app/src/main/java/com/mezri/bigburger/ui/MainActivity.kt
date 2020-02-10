package com.mezri.bigburger.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.mezri.bigburger.R
import com.mezri.bigburger.data.room.AppDatabase
import com.mezri.bigburger.ui.main.MainFragment

class MainActivity : AppCompatActivity(), MainActivityInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // show main fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    MainFragment.newInstance(),
                    MainFragment::class.java.simpleName
                )
                .commitNow()
        }
    }

    /**
     * Show product details fragment
     */
    override fun showFragment(fragment: Fragment, view: View?) {
        supportFragmentManager.beginTransaction().apply {
            if (view != null) {
                // add shared element to fragment transaction
                addSharedElement(view, ViewCompat.getTransitionName(view)!!)
            }

            replace(
                R.id.container,
                fragment,
                fragment::class.java.simpleName
            )

            addToBackStack(fragment::class.java.simpleName)

            commit()
        }
    }
}

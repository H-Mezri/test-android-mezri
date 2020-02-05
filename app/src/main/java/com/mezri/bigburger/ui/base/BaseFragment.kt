package com.mezri.bigburger.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.mezri.bigburger.data.repository.RepositoryImpl
import com.mezri.bigburger.ui.MainActivityInterface
import com.mezri.bigburger.utils.schedulers.SchedulerProvider

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set repository
        getFragmentViewModel().initViewModelDependencies(
            RepositoryImpl((requireActivity() as MainActivityInterface).getAppDataBase()),
            SchedulerProvider()
        )
    }

    override fun onResume() {
        super.onResume()
        // init observers
        initObservers()
    }

    override fun onPause() {
        super.onPause()
        removeObservers()
        getFragmentViewModel().clearTemporaryData()
    }

    abstract fun initObservers()
    abstract fun removeObservers()
    abstract fun getFragmentViewModel(): BaseViewModel
}
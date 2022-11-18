package com.example.dcom.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes val layoutId: Int): Fragment(), BaseView {

    protected lateinit var myInflater: LayoutInflater
    protected lateinit var viewRoot : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onPrepareInitView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::myInflater.isInitialized) {
            myInflater = LayoutInflater.from(requireActivity())
        }
        onInitBinding()
        viewRoot= attachView(inflater, container, savedInstanceState)
        return viewRoot
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        onInitView()
        onObserverViewModel()
    }

    open fun attachView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onInitView() {}

    override fun onInitBinding() {}

}

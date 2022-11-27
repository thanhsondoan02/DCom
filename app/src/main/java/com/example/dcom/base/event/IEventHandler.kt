package com.example.dcom.base.event

import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

interface IEventHandler {
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(event: IEvent)
}

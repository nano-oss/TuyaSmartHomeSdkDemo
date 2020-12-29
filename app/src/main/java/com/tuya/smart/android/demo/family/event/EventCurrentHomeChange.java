package com.tuya.smart.android.demo.family.event;

import com.tuya.smart.home.sdk.bean.HomeBean;

public class EventCurrentHomeChange {

    private HomeBean homeBean;

    public EventCurrentHomeChange(HomeBean homeBean) {
        this.homeBean = homeBean;
    }

    public HomeBean getHomeBean() {
        return homeBean;
    }

    public void setHomeBean(HomeBean homeBean) {
        this.homeBean = homeBean;
    }
}

package com.tuya.smart.android.demo.personal;



import com.tuya.smart.home.sdk.bean.PersonBean;

import java.util.ArrayList;

/**
 * Created by leaf on 15/12/21.
 * 共享
 */
public interface ISharedSentView {
    void finishLoad();

    void updateList(ArrayList<PersonBean> members);

    void updateList(PersonBean member);

    void updateList(PersonBean member, int position);

    void reloadBaseView();
}

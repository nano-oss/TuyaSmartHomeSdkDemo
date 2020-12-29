package com.tuya.smart.android.demo.scene.bean;

import java.io.Serializable;

/**
 * create by nielev on 2019-10-30
 */
public class OperatorBean implements Serializable {
    private Object key;
    private String value;
    private boolean checked;
    public OperatorBean(Object key, String value){
        this.key = key;
        this.value = value;
    }
    public OperatorBean(){}

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

package com.tuya.smart.android.demo.family.recyclerview.item;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * 每个itemView的view持有者
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void setVisibility(int visibility) {
        itemView.setVisibility(visibility);
    }

    /**
     * 获取ItemView
     */
    public View getItemView() {
        return itemView;
    }

    public Context getContext() {
        return itemView.getContext();
    }

    public String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    public String getString(@StringRes int resId, Object... formatArgs) {
        return getContext().getString(resId, formatArgs);
    }

    public String[] getStringArray(@ArrayRes int resId) {
        return getContext().getResources().getStringArray(resId);
    }

    public String getIcon(@StringRes int resId) {
        return getContext().getString(resId);
    }

    public int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    public Drawable getDrawable(@DrawableRes int resId) {
        return ContextCompat.getDrawable(getContext(), resId);
    }

    public Resources getResources(){
        return getContext().getResources();
    }

}

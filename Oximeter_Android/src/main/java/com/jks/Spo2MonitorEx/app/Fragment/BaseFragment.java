package com.jks.Spo2MonitorEx.app.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jks.Spo2MonitorEx.util.config.Config;

/**
 * Created by apple on 16/9/8.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class BaseFragment extends Fragment {
    protected static Config config;
    protected static Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = (Config) getActivity().getApplicationContext();
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 该函数主要用于判断碎片是否在显示，显示的话则需要加载数据，不显示的话暂时不加载数据，这样可以提高app打开时候的速度
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected void init() {
    }

    protected void loadView() {
    }

    protected void initHandler() {
    }

    protected void showTip(String mString) {
        Toast.makeText(context, mString, Toast.LENGTH_SHORT).show();
    }

    protected void showTip(int mString) {
        Toast.makeText(context, mString, Toast.LENGTH_SHORT).show();
    }
}

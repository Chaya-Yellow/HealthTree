package com.jks.Spo2MonitorEx.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jks.Spo2MonitorEx.app.Activity.HistoryActivity;
import com.jks.Spo2MonitorEx.app.Class.HistoryData;
import com.jks.Spo2MonitorEx.app.Class.HistoryDataLib;
import com.jks.Spo2MonitorEx.R;
import com.jks.Spo2MonitorEx.util.data.MyDateUtil;
import com.jks.Spo2MonitorEx.util.entity.Oximet;
import com.jks.Spo2MonitorEx.util.entity.OximeterAndPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by apple on 16/9/8.
 */

/**
 * 列表数据回顾界面
 */
public class HistoryParamFragment extends BaseFragment {
    private BaseAdapter mBaseAdapter;
    private List<HistoryData> mDatas;
    private List<Oximet> oximets;
    private Handler paramHandle;

    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initHandler();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_params, container, false);

        listView = (ListView) v.findViewById(R.id.param_list);
        listView.setClickable(false);
        initAdapter();

        return v;
    }

    @Override
    protected void init() {
        super.init();
        //        HistoryActivity activity = (HistoryActivity) getActivity();
        mDatas = new ArrayList<>();
        mDatas.addAll(HistoryDataLib.getInstance().mHistoryDatas);
        oximets = new ArrayList<>();
    }

    @Override
    protected void initHandler() {
        super.initHandler();
        paramHandle = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                switch (msg.what) {
                    case HistoryChartFragment.ADDNEWVALUETOCHART:
                        if (MyDateUtil.calendarEqToday(HistoryActivity.selectedDate)) {//如果选择的日期不是今天就不要添加到视图上了
                            Oximet oximet = (Oximet) msg.obj;
//                        oximets.add(oximet);
                            oximets.add(0, oximet);//往前添加
                            initAdapter();
//                        listView.invalidate();
                        }
                        break;
                    case HistoryChartFragment.HADSYNCCHARTDATA:
                        OximeterAndPoint oximeterAndPoint = (OximeterAndPoint) msg.obj;
                        oximets = oximeterAndPoint.getOximets();//获取选择日期的数据
                        Collections.reverse(oximets);//倒序显示
                        initAdapter();
//                        listView.invalidate();
                        break;
                    default:
                        break;
                }
            }
        };
        config.setParamChartHandler(paramHandle);
    }

    private void initAdapter() {
        mBaseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return oximets.size();
            }

            @Override
            public Object getItem(int i) {
                return oximets.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                if (view == null) {
                    view = getActivity().getLayoutInflater().inflate(R.layout.list_item_history, null);
                }

                TextView date = (TextView) view.findViewById(R.id.param_date);
//                date.setText(mDatas.get(i).getDate());
                date.setText(oximets.get(i).getRecord_date());

                TextView spo2 = (TextView) view.findViewById(R.id.param_spo2);
                if (oximets.get(i).getSPO2() == 127) {
                    spo2.setText("--");
                } else {
                    spo2.setText("" + oximets.get(i).getSPO2());
                }

                TextView hr = (TextView) view.findViewById(R.id.param_hr);
                if (oximets.get(i).getPR() == 255) {
                    hr.setText("---");
                } else {
                    hr.setText("" + oximets.get(i).getPR());
                }

//                TextView resp = (TextView) view.findViewById(R.id.param_resp);
//                if (oximets.get(i).getRESP() == 255) {
//                    resp.setText("--");
//                } else {
//                    resp.setText("" + oximets.get(i).getRESP());
//                }
//                resp.setVisibility(View.GONE);

                return view;
            }
        };
        listView.setAdapter(mBaseAdapter);
    }

    public void setParamList(Vector<HistoryData> list) {
        mDatas = list;
    }
}

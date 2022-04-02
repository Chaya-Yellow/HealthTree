package com.jks.Spo2MonitorEx.util.view;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/8/31.
 */
public class ArrayWheelAdapter<T> implements WheelAdapter {

    /** The default items length */
    public static final int DEFAULT_LENGTH = -1;

    // items
    private String items[];
    private List<String> list;
    // length
    private int length;
    /**
     * Constructor
     * @param items the items
     * @param length the max items length
     */
    public ArrayWheelAdapter(String items[], int length) {
        this.items = items;
        this.length = length;
        list = new ArrayList<String>();
        for(String i:items)
            list.add(i);
    }


    /**
     * Contructor
     * @param items the items
     */
    public ArrayWheelAdapter(String items[]) {
        this(items, DEFAULT_LENGTH);
    }
    public ArrayWheelAdapter(List<String> list) {
        this.list = list;
    }

    //	@Override
//	public String getItem(int index) {
//		if (index >= 0 && index < list.size()) {
//			return list.get(index).toString();
//		}
//		return null;
//	}
    @Override
    public String getItem(int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return list.size();
    }

    @Override
    public int getMaximumLength() {
        return length;
    }

}
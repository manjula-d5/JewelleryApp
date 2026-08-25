package com.company.tdybtrfid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Adapter for list view
 */
public class MyAdapter2 extends BaseAdapter {
    private ArrayList<String> list;
    private static HashMap<Integer, Boolean> isSelected;
    private LayoutInflater inflater = null;

    public MyAdapter2(ArrayList<String> list, Context context) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }

    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listitemview_fre, null);

            holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cb.setText((String)getItem(position));
        holder.cb.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        MyAdapter2.isSelected = isSelected;
    }

    public static class ViewHolder {
        CheckBox cb;
    }
}

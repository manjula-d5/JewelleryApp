package com.company.tdybtrfid;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

public class MyAdapter extends SimpleAdapter{
	public MyAdapter(Context context, List<? extends Map<String, ?>> data,
                     int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		cr=Color.WHITE;
	}
    public void setColor(int color)
    {
    	cr=color;
    }
    private int cr;
	@Override      
	public View getView(final int position, View convertView, ViewGroup parent)
	{           
		// TODO Auto-generated method stub 
		// listviewÿ�εõ�һ��item����Ҫviewȥ���ƣ�ͨ��getView�����õ�view           
		// positionΪitem�����          
		//Log.d("MYINFO", "getview:"+String.valueOf(position));
		View view = null;           
		if (convertView != null) {
			view = convertView;
			// ʹ�û����view,��Լ�ڴ�
			// ��listview��item����ʱ���϶�����סһ����item������ס��item��view����convertView�����š�
			// ���������ص�֮ǰ����ס��itemʱ��ֱ��ʹ��convertView����������ȥnew view()
			} else {
				view = super.getView(position, convertView, parent);
				}
		int[] colors = {cr, Color.rgb(219, 238, 244) };//RGB��ɫ 
		 view.setBackgroundColor(Color.rgb(243,230,255));// ÿ��item֮����ɫ��ͬ
		return super.getView(position, view, parent); 
	}
	public void notifyDataSetChanged()
	{
		super.notifyDataSetChanged();
	}
}

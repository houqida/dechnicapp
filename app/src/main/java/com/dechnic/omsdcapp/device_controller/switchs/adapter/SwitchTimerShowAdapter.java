package com.dechnic.omsdcapp.device_controller.switchs.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.entity.TimerEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */

public class SwitchTimerShowAdapter extends BaseAdapter{
    private Context mContext;
    private List<TimerEntity> mData;
    private onSwitchListener listener;

    public void setListener(onSwitchListener listener){
        this.listener = listener;

    }

    public SwitchTimerShowAdapter(Context mContext, List<TimerEntity> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final TimerEntity entity = mData.get(position);
        if (convertView==null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_switch_timer_list,null);
            viewHolder.unUserText=(TextView)convertView.findViewById(R.id.unuser_text) ;
            viewHolder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
            viewHolder.timceTv = (TextView) convertView.findViewById(R.id.timceTv);
            viewHolder.statusTv = (TextView) convertView.findViewById(R.id.statusTv);
            viewHolder.switch_open_Rel = (RelativeLayout) convertView.findViewById(R.id.switch_open_Rel);
            viewHolder.switch_close_Rel = (RelativeLayout) convertView.findViewById(R.id.switch_close_Rel);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.timeTv.setText(entity.getDeviceTime());
        viewHolder.timeTv.getPaint().setFakeBoldText(true);
        viewHolder.timceTv.setText(entity.getDeviceWeek());


        if (entity.getOpen()) {
            viewHolder.timeTv.setTextColor(Color.parseColor("#313131"));
            viewHolder.timceTv.setTextColor(Color.parseColor("#313131"));
            viewHolder.statusTv.setTextColor(Color.parseColor("#313131"));
            viewHolder.switch_close_Rel.setVisibility(View.GONE);
            viewHolder.switch_open_Rel.setVisibility(View.VISIBLE);
            viewHolder.switch_open_Rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null) {
                        listener.onSwitchChanged(false,entity.getId(),entity.getOrder());
                    }
                }
            });
        }else {
            viewHolder.timeTv.setTextColor(Color.parseColor("#999999"));
            viewHolder.timceTv.setTextColor(Color.parseColor("#999999"));
            viewHolder.statusTv.setTextColor(Color.parseColor("#999999"));
            viewHolder.switch_close_Rel.setVisibility(View.VISIBLE);
            viewHolder.switch_open_Rel.setVisibility(View.GONE);
            viewHolder.switch_close_Rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null) {
                        listener.onSwitchChanged(true,entity.getId(),entity.getOrder());
                    }
                }
            });
        }
        if ("1".equals(entity.getTimer())) {//开启定时
            viewHolder.statusTv.setText("定时: 开机");

        }else   if ("2".equals(entity.getTimer())){
            viewHolder.statusTv.setText("定时: 关机");
        }else   if ("0".equals(entity.getTimer())){
            viewHolder.statusTv.setText("定时:未启用");
            viewHolder.unUserText.setVisibility(View.VISIBLE);
            viewHolder.switch_open_Rel.setVisibility(View.GONE);
            viewHolder.switch_close_Rel.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder{
        TextView timeTv,timceTv,statusTv,unUserText;
        RelativeLayout switch_open_Rel,switch_close_Rel;
    }

    public interface onSwitchListener{
        public void onSwitchChanged(boolean status, String id,int order);

    }
}

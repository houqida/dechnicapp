package com.dechnic.omsdcapp.scene.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.entity.SceneDeviceEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/10.
 */

public class SceneDetailsDeviceListAdapter extends BaseAdapter{

    private Context context;
    private List<SceneDeviceEntity> mdata;

    private static final int TYPE_COUNT = 3;//item类型的总数
    private static final int TYPE_TEMP = 0;//温控器
    private static final int TYPE_SWITCH = 1;//插座
    private static final int TYPE_SOCKET = 2;//开关

    private int currentType;//当前item类型

    private onSwitchListener listener;

    public void setListener(onSwitchListener listener) {
        this.listener = listener;

    }


    public SceneDetailsDeviceListAdapter(Context context, List<SceneDeviceEntity> mdata) {
        this.context = context;
        this.mdata = mdata;
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public int getItemViewType(int position) {
        if ("温控器".equals(mdata.get(position).getDeviceKind())) {
            return TYPE_TEMP;
        } else if ("开关".equals(mdata.get(position).getDeviceKind())) {
            return TYPE_SWITCH;
        } else  {
            return TYPE_SOCKET;
        }

    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        currentType = getItemViewType(position);
        final SceneDeviceEntity entity = mdata.get(position);
        if (convertView==null) {
            viewHolder = new ViewHolder();
            switch (currentType){
                case TYPE_TEMP:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_scene_tempcontrol,null);
                    viewHolder.tempnameTv = (TextView) convertView.findViewById(R.id.tempnameTv);
                    viewHolder.tenmTv = (TextView) convertView.findViewById(R.id.tenmTv);
                    viewHolder.switch_open_Rel_temp = (RelativeLayout) convertView.findViewById(R.id.switch_open_Rel);
                    viewHolder.switch_close_Rel_temp = (RelativeLayout) convertView.findViewById(R.id.switch_close_Rel);
                    break;
                case TYPE_SWITCH:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_scene_switchcontrol,null);
                    viewHolder.switch_nameTv = (TextView) convertView.findViewById(R.id.switch_nameTv);
                    viewHolder.switch_open_Rel_sw = (RelativeLayout) convertView.findViewById(R.id.switch_open_Rel);
                    viewHolder.switch_close_Rel_sw = (RelativeLayout) convertView.findViewById(R.id.switch_close_Rel);
                    break;
                case TYPE_SOCKET:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_scene_socketcontrol,null);
                    viewHolder.socket_nameTv = (TextView) convertView.findViewById(R.id.socket_nameTv);
                    viewHolder.switch_open_Rel_so = (RelativeLayout) convertView.findViewById(R.id.switch_open_Rel);
                    viewHolder.switch_close_Rel_so = (RelativeLayout) convertView.findViewById(R.id.switch_close_Rel);
                    break;
            }

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (currentType){
            case TYPE_TEMP:
                viewHolder.tempnameTv.setText(entity.getDeviceName());
                viewHolder.tenmTv.setText(entity.getSpeed()+"   "+entity.getPattern()+" "+entity.getTemperature()+"℃");
                if (entity.getIsOn().equals("1")) {//打开状态
                    viewHolder.switch_open_Rel_temp.setVisibility(View.VISIBLE);
                    viewHolder.switch_close_Rel_temp.setVisibility(View.GONE);
                    viewHolder.switch_open_Rel_temp.setOnClickListener(new View.OnClickListener() {//执行关闭操作
                        @Override
                        public void onClick(View v) {
                            execute(entity.getDeviceId(),"0");
                        }
                    });

                }else {//关闭状态
                    viewHolder.switch_open_Rel_temp.setVisibility(View.GONE);
                    viewHolder.switch_close_Rel_temp.setVisibility(View.VISIBLE);
                    viewHolder.switch_close_Rel_temp.setOnClickListener(new View.OnClickListener() {//执行打开操作
                        @Override
                        public void onClick(View v) {
                            execute(entity.getDeviceId(),"1");
                        }
                    });

                }
                break;
            case TYPE_SWITCH:
                viewHolder.switch_nameTv.setText(entity.getDeviceName());
                if (entity.getIsOn().equals("1")) {
                    viewHolder.switch_open_Rel_sw.setVisibility(View.VISIBLE);
                    viewHolder.switch_close_Rel_sw.setVisibility(View.GONE);
                    viewHolder.switch_open_Rel_sw.setOnClickListener(new View.OnClickListener() {//执行关闭操作
                        @Override
                        public void onClick(View v) {
                            execute(entity.getDeviceId(),"0");
                        }
                    });
                }else {
                    viewHolder.switch_open_Rel_sw.setVisibility(View.GONE);
                    viewHolder.switch_close_Rel_sw.setVisibility(View.VISIBLE);
                    viewHolder.switch_close_Rel_sw.setOnClickListener(new View.OnClickListener() {//执行打开操作
                        @Override
                        public void onClick(View v) {
                            execute(entity.getDeviceId(),"1");
                        }
                    });
                }
                break;
            case TYPE_SOCKET:
                viewHolder.socket_nameTv.setText(entity.getDeviceName());
                if (entity.getIsOn().equals("1")) {
                    viewHolder.switch_open_Rel_so.setVisibility(View.VISIBLE);
                    viewHolder.switch_close_Rel_so.setVisibility(View.GONE);
                    viewHolder.switch_open_Rel_so.setOnClickListener(new View.OnClickListener() {//执行关闭操作
                        @Override
                        public void onClick(View v) {
                            execute(entity.getDeviceId(),"0");
                        }
                    });
                }else {
                    viewHolder.switch_open_Rel_so.setVisibility(View.GONE);
                    viewHolder.switch_close_Rel_so.setVisibility(View.VISIBLE);
                    viewHolder.switch_close_Rel_so.setOnClickListener(new View.OnClickListener() {//执行打开操作
                        @Override
                        public void onClick(View v) {
                            execute(entity.getDeviceId(),"1");
                        }
                    });
                }
                break;

        }
        return convertView;
    }

    private void execute(String id,String on){
        if (listener!=null) {
            listener.onSwitchChanged(id,on);
        }
    }

    class ViewHolder{
        TextView  tempnameTv,tenmTv;
        RelativeLayout switch_open_Rel_temp,switch_close_Rel_temp;


        TextView switch_nameTv;
        RelativeLayout switch_open_Rel_sw,switch_close_Rel_sw;

        TextView socket_nameTv;
        RelativeLayout switch_open_Rel_so,switch_close_Rel_so;

    }
    public interface onSwitchListener {
        void onSwitchChanged(String id, String on);
    }

}

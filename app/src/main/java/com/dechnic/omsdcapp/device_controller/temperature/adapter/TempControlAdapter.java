package com.dechnic.omsdcapp.device_controller.temperature.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.entity.ControlEntity;
import com.dechnic.omsdcapp.widget.AlertDialog;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 */

public class TempControlAdapter extends BaseAdapter {
    private Context mContext;
    private List<ControlEntity> mData;
    private onSwitchListener listener;

    public void setListener(onSwitchListener listener) {
        this.listener = listener;

    }

    public TempControlAdapter(Context mContext, List<ControlEntity> mData) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final ControlEntity controlEntity = mData.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tempcontrol, null);
            viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.iconIv);
            viewHolder.tempnameTv = (TextView) convertView.findViewById(R.id.tempnameTv);
            viewHolder.tenmTv = (TextView) convertView.findViewById(R.id.tenmTv);
            viewHolder.switch_open_Rel = (RelativeLayout) convertView.findViewById(R.id.switch_open_Rel);
            viewHolder.switch_close_Rel = (RelativeLayout) convertView.findViewById(R.id.switch_close_Rel);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String disa = "";
        viewHolder.tempnameTv.setText(controlEntity.getControlName());
        if (controlEntity.getPanelDisable()==0) {
            disa = "面板已禁用";
        }
        if (controlEntity.isOnline()) {
            viewHolder.tempnameTv.setTextColor(Color.parseColor("#000000"));
            viewHolder.tenmTv.setText("在线  " + "室温 "+controlEntity.getIndoorTemperature()+"℃   "+disa);
            viewHolder.tenmTv.setTextColor(Color.parseColor("#666666"));
            viewHolder.iconIv.setImageResource(R.mipmap.online_icon);
            if (controlEntity.isOpen()) {
                viewHolder.switch_open_Rel.setVisibility(View.VISIBLE);
                viewHolder.switch_close_Rel.setVisibility(View.GONE);
                viewHolder.switch_open_Rel.setOnClickListener(new View.OnClickListener() {//执行关闭操作
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onSwitchChanged(false, mData.get(position).getControlId());
                        }
                    }
                });
            }else {
                viewHolder.switch_open_Rel.setVisibility(View.GONE);
                viewHolder.switch_close_Rel.setVisibility(View.VISIBLE);
                viewHolder.switch_close_Rel.setOnClickListener(new View.OnClickListener() {//执行打开操作
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onSwitchChanged(true, controlEntity.getControlId());
                        }
                    }
                });
            }

        } else {
            viewHolder.tenmTv.setText("离线  " + "室温 "+controlEntity.getIndoorTemperature()+"℃   "+disa);
            viewHolder.tempnameTv.setTextColor(Color.parseColor("#999999"));
            viewHolder.tenmTv.setTextColor(Color.parseColor("#999999"));
            viewHolder.iconIv.setImageResource(R.mipmap.offline_icon);
            if (controlEntity.isOpen()) {
                viewHolder.switch_open_Rel.setVisibility(View.VISIBLE);
                viewHolder.switch_close_Rel.setVisibility(View.GONE);
                viewHolder.switch_open_Rel.setBackgroundResource(R.drawable.switch_close_background);
                viewHolder.switch_open_Rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOfflineDialog();
                    }
                });
            }else {
                viewHolder.switch_close_Rel.setVisibility(View.VISIBLE);
                viewHolder.switch_open_Rel.setVisibility(View.GONE);
                viewHolder.switch_close_Rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOfflineDialog();
                    }
                });
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iconIv;
        TextView tempnameTv, tenmTv;
        RelativeLayout switch_open_Rel, switch_close_Rel;
    }

    public interface onSwitchListener {
        void onSwitchChanged(boolean status, String id);
    }

    private void showOfflineDialog(){
        new AlertDialog(mContext).builder().setTitle("提示" )
                .setMsg("现在处于离线状态，无法执行该操作")
                .setPositiveButton("确认", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }
}

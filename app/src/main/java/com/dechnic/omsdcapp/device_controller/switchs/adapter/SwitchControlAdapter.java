package com.dechnic.omsdcapp.device_controller.switchs.adapter;

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

public class SwitchControlAdapter extends BaseAdapter {
    private Context mContext;
    private List<ControlEntity> mData;
    private onSwitchListener listener;

    public void setListener(onSwitchListener listener) {
        this.listener = listener;

    }

    public SwitchControlAdapter(Context mContext, List<ControlEntity> mData) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_switchcontrol, null);
            viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.iconIv);
            viewHolder.switch_nameTv = (TextView) convertView.findViewById(R.id.switch_nameTv);
            viewHolder.switch_statusTv = (TextView) convertView.findViewById(R.id.switch_statusTv);
            viewHolder.switch_open_Rel = (RelativeLayout) convertView.findViewById(R.id.switch_open_Rel);
            viewHolder.switch_close_Rel = (RelativeLayout) convertView.findViewById(R.id.switch_close_Rel);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.switch_nameTv.setText(controlEntity.getControlName());
        String dis = "";
        if (controlEntity.getPanelDisable()==0) {
            dis = "面板已禁用";
        }
        if (controlEntity.isOnline()) {
            viewHolder.switch_nameTv.setTextColor(Color.parseColor("#000000"));
            viewHolder.switch_statusTv.setText("在线   "+dis);
            viewHolder.switch_statusTv.setTextColor(Color.parseColor("#666666"));
            viewHolder.iconIv.setImageResource(R.mipmap.switch_online_icon);
            if (controlEntity.isOpen()) {
                viewHolder.switch_open_Rel.setVisibility(View.VISIBLE);
                viewHolder.switch_open_Rel.setBackgroundResource(R.drawable.switch_open_background);
                viewHolder.switch_close_Rel.setVisibility(View.GONE);
                viewHolder.switch_open_Rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener!=null) {
                            listener.onSwitchChanged(false, mData.get(position).getControlId());
                        }
                    }
                });
            }else {
                viewHolder.switch_open_Rel.setVisibility(View.GONE);
                viewHolder.switch_close_Rel.setVisibility(View.VISIBLE);
                viewHolder.switch_close_Rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener!=null) {
                            listener.onSwitchChanged(true, mData.get(position).getControlId());
                        }
                    }
                });
            }

        } else {
            viewHolder.switch_statusTv.setText("离线   "+dis);
            viewHolder.switch_nameTv.setTextColor(Color.parseColor("#999999"));
            viewHolder.switch_statusTv.setTextColor(Color.parseColor("#999999"));
            viewHolder.iconIv.setImageResource(R.mipmap.switch_offline_icon);
            if (controlEntity.isOpen()) {
                viewHolder.switch_open_Rel.setVisibility(View.VISIBLE);
                viewHolder.switch_open_Rel.setBackgroundResource(R.drawable.switch_close_background);
                viewHolder.switch_close_Rel.setVisibility(View.GONE);
                viewHolder.switch_open_Rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOfflineDialog();
                    }
                });
            } else {
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
        TextView switch_nameTv, switch_statusTv;
        RelativeLayout switch_open_Rel, switch_close_Rel;
    }

    public interface onSwitchListener {
        void onSwitchChanged(boolean status, String id);
    }

    private void showOfflineDialog() {
        new AlertDialog(mContext).builder().setTitle("提示")
                .setMsg("现在处于离线状态，无法执行该操作")
                .setPositiveButton("确认", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }
}

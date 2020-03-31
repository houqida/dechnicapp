package com.dechnic.omsdcapp.scene.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.entity.SceneDeviceListEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class SceneAddDeviceListAdapter extends BaseAdapter{

    private Context context;
    private List<SceneDeviceListEntity> mData;

    public SceneAddDeviceListAdapter(Context context, List<SceneDeviceListEntity> mData) {
        this.context = context;
        this.mData = mData;
    }

    private onAddOrDelteListener listener;

    public void setListener(onAddOrDelteListener listener){
        this.listener = listener;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final SceneDeviceListEntity entity = mData.get(position);
        if (convertView==null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_scene_devicelist_add,null);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.iconIv);
            viewHolder.deviceName = (TextView) convertView.findViewById(R.id.device_nameTv);
            viewHolder.addTv = (TextView) convertView.findViewById(R.id.addTv);
            viewHolder.delteTv = (TextView) convertView.findViewById(R.id.delteTv);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.deviceName.setText(entity.getDeviceName());

        if (entity.getDeviceKind().equals("温控器")) {
            viewHolder.icon.setImageResource(R.mipmap.online_icon);
        } else if (entity.getDeviceKind().equals("开关")) {
            viewHolder.icon.setImageResource(R.mipmap.switch_online_icon);
        } else if (entity.getDeviceKind().equals("插座")) {
            viewHolder.icon.setImageResource(R.mipmap.socket_online);

        }

        if (entity.getFlag().equals("1")) {//场景设备中存在，应该显示移除
            viewHolder.delteTv.setVisibility(View.VISIBLE);
            viewHolder.addTv.setVisibility(View.GONE);
            viewHolder.delteTv.setOnClickListener(new View.OnClickListener() {//点击，执行“移除”操作
                @Override
                public void onClick(View v) {
                    if (listener!=null) {
                        listener.onChanged(entity.getId(),"1");
                    }
                }
            });

        }else {
            viewHolder.delteTv.setVisibility(View.GONE);
            viewHolder.addTv.setVisibility(View.VISIBLE);
            viewHolder.addTv.setOnClickListener(new View.OnClickListener() {//点击，执行“添加”操作
                @Override
                public void onClick(View v) {
                    if (listener!=null) {
                        listener.onChanged(entity.getId(),"2");
                    }
                }
            });
        }
        return convertView;
    }
    class ViewHolder{
        ImageView icon;
        TextView deviceName;
        TextView addTv;
        TextView delteTv;
    }

    public interface onAddOrDelteListener{
        void onChanged(String id,String flag);

    }
}

package com.dechnic.omsdcapp.scene.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.entity.SceneEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class SceneShowGridAdapter extends BaseAdapter{
    private Context context;
    private List<SceneEntity> mdata;


    public SceneShowGridAdapter(Context context, List<SceneEntity> mdata) {
        this.context = context;
        this.mdata = mdata;
    }

    @Override
    public int getCount() {
        return mdata.size();
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
        if (convertView==null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_scene_gridview,null);
            viewHolder.scene_textView = (TextView) convertView.findViewById(R.id.scene_tv);
            viewHolder.scene_imageView = (ImageView) convertView.findViewById(R.id.scene_iv);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.scene_textView.setText(mdata.get(position).getSceneName());
        if (mdata.get(position).getSceneIv().equals("1")) {
            viewHolder.scene_imageView.setImageResource(R.mipmap.job_modle_icon);
        } else if (mdata.get(position).getSceneIv().equals("2")) {
            viewHolder.scene_imageView.setImageResource(R.mipmap.rest_modle_icon);
        }else if (mdata.get(position).getSceneIv().equals("3")) {
            viewHolder.scene_imageView.setImageResource(R.mipmap.out_modle_icon);
        } else if (mdata.get(position).getSceneIv().equals("4")) {
            viewHolder.scene_imageView.setImageResource(R.mipmap.longrest_modle_icon);
        } else if (mdata.get(position).getSceneIv().equals("6")) {
            viewHolder.scene_imageView.setImageResource(R.mipmap.green_modle_icon);
        } else if (mdata.get(position).getSceneIv().equals("5")) {
            viewHolder.scene_imageView.setImageResource(R.mipmap.week_modle_icon);
        } else if (mdata.get(position).getSceneIv().equals("7")) {
            viewHolder.scene_imageView.setImageResource(R.mipmap.add_modle_icon);
        }

        return convertView;
    }
    class ViewHolder{
        ImageView scene_imageView;
        TextView scene_textView;
    }
}

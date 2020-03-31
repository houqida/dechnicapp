package com.dechnic.omsdcapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.entity.HelpMessage;

import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class HelpMessageListAdapter extends BaseAdapter{
    private Context context;
    private List<HelpMessage> helpMessages;

    public HelpMessageListAdapter(Context context, List<HelpMessage> helpMessages) {
        this.context = context;
        this.helpMessages = helpMessages;
    }

    @Override
    public int getCount() {
        return helpMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return helpMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        HelpMessage help = helpMessages.get(position);
        if (convertView==null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_help_list,null);
            holder.title = (TextView) convertView.findViewById(R.id.help_title_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(help.getTitle());

        return convertView;
    }

    class ViewHolder{
        TextView title;
    }
}

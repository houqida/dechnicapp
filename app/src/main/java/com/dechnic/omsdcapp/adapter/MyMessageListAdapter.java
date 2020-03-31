package com.dechnic.omsdcapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dechnic.omsdcapp.R;
import com.dechnic.omsdcapp.entity.InfoMessage;

import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class MyMessageListAdapter extends BaseAdapter{

    private Context context;
    private List<InfoMessage> messages;

    public MyMessageListAdapter(Context context, List<InfoMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        InfoMessage message = messages.get(position);
        if (convertView==null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_message_list,null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.messageTitleTv);
            viewHolder.content = (TextView) convertView.findViewById(R.id.messageContentTv);
            viewHolder.time = (TextView) convertView.findViewById(R.id.messageTimeTv);
           convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(message.getTitle());
        viewHolder.content.setText(message.getContent());
        viewHolder.time.setText(message.getUpdatedOn());
        return convertView;
    }

    class ViewHolder{
        TextView title;
        TextView content;
        TextView time;
    }


}

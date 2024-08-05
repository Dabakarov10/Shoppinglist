package com.dabakarov10.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class EventCustomAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView eventName;
        TextView countUsers;
        TextView date_txt;
    }

    public EventCustomAdapter(ArrayList<Event> data, Context context) {
        super(context, R.layout.home_page_item, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Event getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_item, parent, false);
            viewHolder.eventName = (TextView) convertView.findViewById(R.id.eventName);
            viewHolder.countUsers = (TextView) convertView.findViewById(R.id.countUsers);
            viewHolder.date_txt = (TextView) convertView.findViewById(R.id.date_txt);

            result = convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Event item = getItem(position);

        viewHolder.eventName.setText(item.eventName);
        viewHolder.countUsers.setText(item.eventCountUsers);
        viewHolder.date_txt.setText(item.eventDate);

        return result;
    }
}

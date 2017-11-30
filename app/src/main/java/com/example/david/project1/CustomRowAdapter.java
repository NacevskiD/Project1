package com.example.david.project1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by David on 11/29/2017.
 */

public class CustomRowAdapter extends ArrayAdapter<ListItem> {
    Context mContext;
    int layoutResourceId;
    ArrayList<ListItem> data = null;
    //ListItem data[] = null;
    public CustomRowAdapter(Context context,int layoutResourceId, ArrayList <ListItem> data){
        super(context,layoutResourceId,data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        DataHolder holder = null;
        if (row == null){
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId,parent,false);
            holder = new DataHolder();
            holder.image = (ImageView) row.findViewById(R.id.image_view);
            holder.desc = (TextView) row.findViewById(R.id.desc_view);
            holder.date = (TextView) row.findViewById(R.id.date_view);

            row.setTag(holder);
        }
        else {
            holder = (DataHolder)row.getTag();
        }

        ListItem listItem = data.get(position);
        holder.desc.setText(listItem.desc);
        holder.date.setText(listItem.currentDateTimeString);
        holder.image.setImageBitmap(listItem.pic);

        return row;
    }

    static class DataHolder{
        ImageView image;
        TextView desc;
        TextView date;
    }
}

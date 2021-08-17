package com.sign.language.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.sign.language.R;
import com.sign.language.model.ImageItem;


public class GridViewAdapter extends BaseAdapter {
    private Context context;
    int [] photos;
    LayoutInflater inflater;

    public GridViewAdapter(Context context,int []photos){
        this.context=context;
        this.photos=photos;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return photos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.row_grid, null); // inflate the layout
        ImageView icon = convertView.findViewById(R.id.image); // get the reference of ImageView
        icon.setImageResource(photos[position]); // set logo images
        return convertView;
    }
}

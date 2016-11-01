package com.study.android.ahu.experimentfour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ahu on 16-10-21.
 */
public class FruitAdapter extends BaseAdapter {

    private class ViewHolder {
        public ImageView fruitImage;
        public TextView fruitName;
    }

    private List<Fruit> list;
    private Context context;

    public FruitAdapter(Context context, List<Fruit> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        if (list == null) {
            return null;
        }
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View convertView;
        ViewHolder viewHolder;

        Fruit fruit = (Fruit)getItem(position);

        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.fruitImage = (ImageView) convertView.findViewById(R.id.fruitImage);
            viewHolder.fruitName = (TextView) convertView.findViewById(R.id.fruitName);
            convertView.setTag(viewHolder);
        } else {
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.fruitImage.setImageResource(fruit.GetImageId());
        viewHolder.fruitName.setText(fruit.GetFruitName());
        return convertView;
    }

}

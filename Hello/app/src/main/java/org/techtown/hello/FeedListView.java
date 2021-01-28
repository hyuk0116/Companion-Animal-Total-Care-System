package org.techtown.hello;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;

import java.util.ArrayList;

public class FeedListView extends BaseAdapter {

    ArrayList<Feed> datas;
    LayoutInflater inflater;

    FeedListView(LayoutInflater inflater, ArrayList<Feed> datas) {
        this.datas = datas;
        this.inflater = inflater;
    }

    public FeedListView(){}

    @Override
    public int getCount () {
        return datas.size();
    }

    public Object getItem (int position){
        return datas.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_row, null);
        }

        TextView text_company = (TextView)convertView.findViewById(R.id.text_company);
        TextView text_name = (TextView)convertView.findViewById(R.id.text_name);
        TextView text_price = (TextView)convertView.findViewById(R.id.text_price);
        ImageView image_feed = (ImageView)convertView.findViewById(R.id.img_feed);

        text_company.setText(datas.get(position).getFCompany());
        text_name.setText(datas.get(position).getFName());
        text_price.setText(Integer.toString(datas.get(position).getFPrice()));
        LoadImage loadImage = new LoadImage(datas.get(position).getFImg());
        Bitmap bitmap = loadImage.getBitmap();
        image_feed.setImageBitmap(bitmap);

        return convertView;
    }
}

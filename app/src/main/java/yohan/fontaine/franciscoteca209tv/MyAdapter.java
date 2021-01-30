package yohan.fontaine.franciscoteca209tv;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> images;
    private ArrayList<String> titles;

    public MyAdapter(Context context, ArrayList<String> images, ArrayList<String> titles) {
        this.context = context;
        this.images = images;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context,R.layout.list_item,null);
        }
        ImageView img = convertView.findViewById(R.id.imageView);
        TextView text = convertView.findViewById(R.id.textTitle);
        Picasso.with(context).load(images.get(position)).into(img);
        text.setText(titles.get(position));
        return convertView;
    }
}

package yohan.fontaine.franciscoteca209tv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<UploadPDF> {
    public ListViewAdapter(Context context, int resource, List<UploadPDF> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item,null);
        }
        UploadPDF uploadPDF = getItem(position);
        ImageView img = convertView.findViewById(R.id.imageView);
        TextView textTitle = convertView.findViewById(R.id.textTitle);
        TextView textAuthor = convertView.findViewById(R.id.textAuthor);

        Picasso.with(getContext()).load(uploadPDF.getImageUrl()).into(img);
        textTitle.setText(uploadPDF.getTitle());
        textAuthor.setText(uploadPDF.getAuthor());

        return convertView;
    }
}

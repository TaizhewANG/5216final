package au.edu.canberra.mtfinalassignment;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
// Please do not change this class name
public class ClassifiedItemAdapter extends ArrayAdapter<ClassifiedItem> {
    ArrayList<ClassifiedItem>events;

    public ClassifiedItemAdapter(Context context , int resource , ArrayList<ClassifiedItem> objects) {
        super(context , resource , objects);
        events = objects;
    }

    @Override
    public View getView(int position , View convertView , ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.my_listview_item , parent , false);
        }


        ClassifiedItem item = events.get(position);

       ImageView icon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
   //   setIconImage(item.getImageFileName() , convertView);

        TextView title = (TextView) convertView.findViewById(R.id.textViewTitle);
        title.setText(item.getItemName());

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#e6e6e6"));
        }

        return convertView;
    }



}
package com.example.android.spaceinvaders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class LaunchAdapter extends ArrayAdapter<Launch> {

    public LaunchAdapter(Context context, List<Launch> launches) {
        super(context, 0, launches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item, parent, false);
        }

        Launch curLaunch = getItem(position);
        TextView name = listItemView.findViewById(R.id.name);
        ImageView image = listItemView.findViewById(R.id.image);
        int index = curLaunch.getDate().indexOf(",");
        name.setText(curLaunch.getRocketName() + "\n" + curLaunch.getDate().substring(0, index + 6));
        String imgURI = curLaunch.getImgURL();
        Picasso.with(listItemView.getContext()).load(imgURI).fit().centerCrop().into(image);

        return listItemView;
    }
}

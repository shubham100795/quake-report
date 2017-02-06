package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shubham arora on 08-01-2017.
 */

public class quakeAdapter extends ArrayAdapter<quake> {

    public quakeAdapter(Activity context, ArrayList<quake> earthquakes)
    {
        super(context,0,earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        quake currentword=getItem(position);
        //setting magnitude
        TextView magtextview=(TextView)listItemView.findViewById(R.id.magnitude_text_view);
        String formattedMagnitude = formatMagnitude(currentword.getmagnitude());
        magtextview.setText(formattedMagnitude);
        GradientDrawable magnitudeCircle = (GradientDrawable) magtextview.getBackground();//taking color of circle not magnitude_textview
        int magnitudeColor = getMagnitudeColor(currentword.getmagnitude());
        magnitudeCircle.setColor(magnitudeColor);
        //setting primary location and location offset
        String place=currentword.getplace();
        int len=place.length();
        int indx=place.indexOf("of");
        if(indx!=-1)
        {
            String off_loc=place.substring(0,indx+2);
            String pri_loc=place.substring(indx+2,len);

            TextView loc2=(TextView)listItemView.findViewById(R.id.location_offset);
            loc2.setText(off_loc);

            TextView loc1=(TextView)listItemView.findViewById(R.id.primary_location);
            loc1.setText(pri_loc);
        }
        else
        {
            String off_loc="Near the";
            String pri_loc=place;

            TextView loc2=(TextView)listItemView.findViewById(R.id.location_offset);
            loc2.setText(off_loc);

            TextView loc1=(TextView)listItemView.findViewById(R.id.primary_location);
            loc1.setText(pri_loc);
        }
        //setting date and time
        Date dateObject = new Date(currentword.getdate());
        String formattedDate = formatDate(dateObject);
        TextView dattextview=(TextView)listItemView.findViewById(R.id.date_text_view);
        dattextview.setText(formattedDate);

        TextView timtextview=(TextView)listItemView.findViewById(R.id.time_text_view);
        String formattedTime = formatTime(dateObject);
        timtextview.setText(formattedTime);

        return listItemView;


    }
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude)
    {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch(magnitudeFloor)
        {
            case 0:
            case 1: magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2: magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3: magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4: magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5: magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6: magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7: magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8: magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9: magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return  ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}

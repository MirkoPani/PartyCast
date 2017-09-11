package com.example.mirko.partycast.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.mirko.partycast.R;
import com.example.mirko.partycast.customviews.CustomFontTextView;
import com.example.mirko.partycast.models.classificaItem;

import java.util.ArrayList;

/**
 * Creato da Mirko Pani e Nicola Gilberti per il progetto del corso LPSMT 2017
 */

/*
Custom Adapter per la presentazione della classifica finale (fragment endGame)

 */
public class classificaAdapter extends ArrayAdapter<classificaItem> {

    private int colorResourceId;

    public classificaAdapter(Activity context, ArrayList<classificaItem> items, int colorResourceId) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, items);
        this.colorResourceId=colorResourceId;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.endgame_classifica_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        classificaItem currentItem = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        CustomFontTextView nameTextView = (CustomFontTextView) listItemView.findViewById(R.id.classificaItem_playerName);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        nameTextView.setText(currentItem.getName());

        // Find the TextView in the list_item.xml layout with the ID version_number
        CustomFontTextView pointsTextView = (CustomFontTextView) listItemView.findViewById(R.id.classificaItem_playerPoints);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        pointsTextView.setText(currentItem.getPoints());

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.classificaItem_playerAvatar);

            imageView.setImageResource(currentItem.getAvatarImgId());
            imageView.setVisibility(View.VISIBLE);

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView

        View textContainer=listItemView.findViewById(R.id.classificaItemLayout);
        int color= ContextCompat.getColor(getContext(),colorResourceId);
        textContainer.setBackgroundColor(color);
        return listItemView;
    }
}

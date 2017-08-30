package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by oogunyinka on 30/08/2017.
 */

public class PetCursorAdapter extends CursorAdapter {


    public PetCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.pet_list_item,parent, false); //inflate the adapter with the pet item layout
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView petNameView = (TextView) view.findViewById(R.id.pet_name);
        TextView petBreedView = (TextView) view.findViewById(R.id.pet_breed);

        String name = cursor.getString(cursor.getColumnIndex("name"));       // get the name at the current cursor
        String breed = cursor.getString(cursor.getColumnIndex("breed")); // get the breed at the current cursor

       //Set current name and breed to respective textviews.
        petNameView.setText(name);
        petBreedView.setText(breed);
    }
}

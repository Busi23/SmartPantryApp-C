package com.example.smartpantryapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

    public class IngredientAdapter extends ArrayAdapter<Ingredients> {

        public IngredientAdapter(Context context,
                                 ArrayList<Ingredients> ingredients) {
            super(context, android.R.layout.simple_list_item_2, ingredients);
        }

        @Override
        public View getView(int position, View row, ViewGroup parent) {

            if (row == null) {
                row = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_2, parent, false);
            }

            TextView nameText = row.findViewById(android.R.id.text1);
            TextView detailsText = row.findViewById(android.R.id.text2);

            Ingredients ingredient = getItem(position);

            nameText.setText(ingredient.name);

            String details = ingredient.quantity + " " + ingredient.unit;

            if (ingredient.expiryDate != null
                    && !ingredient.expiryDate.isEmpty()) {
                details = details + " | Expires: " + ingredient.expiryDate;
            }

            detailsText.setText(details);

            return row;
        }
    }


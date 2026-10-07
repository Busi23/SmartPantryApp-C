package com.example.smartpantryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Toast;

import java.util.ArrayList;

public class PantryActivity extends AppCompatActivity {

    ListView pantryList;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantry);

        database = new Database(this);

        pantryList = findViewById(R.id.listPantryItems);

        pantryList.setEmptyView(findViewById(R.id.textEmptyPantry));
        pantryList.setOnItemClickListener((parent, view, position, id) -> {


                    Ingredients ingredient =
                            (Ingredients) parent.getItemAtPosition(position);

                    Intent intent = new Intent(
                            PantryActivity.this, EditAddIngredient.class);

                    intent.putExtra("id", ingredient.id);
                    intent.putExtra("name", ingredient.name);
                    intent.putExtra("quantity", ingredient.quantity);
                    intent.putExtra("unit", ingredient.unit);
            intent.putExtra("expiry", ingredient.expiryDate);
            startActivity(intent);
                });



        Button addIngredientButton =
                findViewById(R.id.buttonAddIngredient);

        addIngredientButton.setOnClickListener(e -> {
            Intent intent = new Intent(
                    PantryActivity.this, EditAddIngredient.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            bars.left, bars.top, bars.right, bars.bottom);

                    return insets;
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Ingredients> ingredients = database.getIngredients();

        IngredientAdapter adapter =
                new IngredientAdapter(this, ingredients);

        pantryList.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
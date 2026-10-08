package com.example.smartpantryapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;

import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {

    Database database;
    ListView recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);

        database = new Database(this);

        recipeList = findViewById(R.id.listRecipes);
        recipeList.setEmptyView(findViewById(R.id.textNoRecipes));
        Button backButton = findViewById(R.id.buttonBackToPantry);

        backButton.setOnClickListener(e -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (view, insets) -> {

                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    view.setPadding(
                            bars.left + 24, bars.top + 24,
                            bars.right + 24, bars.bottom + 24);

                    return insets;
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Recipe> recipes = database.getSuggestedRecipes();

        ArrayAdapter<Recipe> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, recipes);

        recipeList.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}

package com.example.smartpantryapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);

        TextView recipeName = findViewById(R.id.textRecipeName);
        TextView ingredients = findViewById(R.id.textRecipeIngredients);
        TextView steps = findViewById(R.id.textRecipeSteps);
        Button backButton = findViewById(R.id.buttonBackToRecipes);

        SharedPreferences preferences =
                getSharedPreferences("settings", MODE_PRIVATE);

        if (preferences.getBoolean("largeText", false)) {
            ingredients.setTextSize(22);
            steps.setTextSize(22);
        }

        int recipeId = getIntent().getIntExtra("id", -1);

        recipeName.setText(getIntent().getStringExtra("name"));
        steps.setText(getIntent().getStringExtra("steps"));

        Database database = new Database(this);
        ingredients.setText(database.getRecipeIngredients(recipeId));
        database.close();

        backButton.setOnClickListener(e -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (view, insets) -> {

                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    view.setPadding(
                            bars.left, bars.top, bars.right, bars.bottom);

                    return insets;
                });
    }
}

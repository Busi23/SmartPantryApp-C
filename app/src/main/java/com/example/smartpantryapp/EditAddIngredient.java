package com.example.smartpantryapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class EditAddIngredient extends AppCompatActivity {

    EditText ingredientName, ingredientQuantity, ingredientExpiry;
    Spinner ingredientUnit;
    Database database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_add_ingredient);

        ingredientName = findViewById(R.id.editIngredientName);
        ingredientQuantity = findViewById(R.id.editQuantity);
        ingredientExpiry = findViewById(R.id.editExpiryDate);
        ingredientUnit = findViewById(R.id.spinnerUnit);


        Button saveButton = findViewById(R.id.buttonSaveIngredient);
        Button cancelButton = findViewById(R.id.buttonCancel);
        database = new Database(this);

        saveButton.setOnClickListener(e -> {
            saveIngredient();
        });

        cancelButton.setOnClickListener(e -> {
            finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void saveIngredient() {
        String name = ingredientName.getText().toString().trim();
        String amount = ingredientQuantity.getText().toString().trim();
        String unit = ingredientUnit.getSelectedItem().toString();
        String expiry = ingredientExpiry.getText().toString().trim();

        if (name.isEmpty()) {
            ingredientName.setError("Enter an ingredient name");
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            ingredientQuantity.setError("Enter a valid number");
            return;
        }

        if (quantity <= 0 || Double.isNaN(quantity)
                || Double.isInfinite(quantity)) {
            ingredientQuantity.setError("Enter a number greater than zero");
            return;
        }

        if (!expiry.isEmpty() && !validDate(expiry)) {
            ingredientExpiry.setError("Use a valid date: YYYY-MM-DD");
            return;
        }

        long result = database.addIngredient(name, quantity, unit, expiry);

        if (result == -1) {
            Toast.makeText(this, "Ingredient could not be saved",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ingredient saved",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public boolean validDate(String date) {
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }

        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        format.setLenient(false);

        ParsePosition position = new ParsePosition(0);

        return format.parse(date, position) != null
                && position.getIndex() == date.length();
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }




}
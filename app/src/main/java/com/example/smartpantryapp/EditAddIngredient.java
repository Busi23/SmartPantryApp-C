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
import android.view.View;
import androidx.appcompat.app.AlertDialog;

public class EditAddIngredient extends AppCompatActivity {

    EditText ingredientName, ingredientQuantity, ingredientExpiry;
    Spinner ingredientUnit;
    Database database;
    int ingredientId = -1;



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
        ingredientId = getIntent().getIntExtra("id", -1);

        if (ingredientId != -1) {
            ingredientName.setText(getIntent().getStringExtra("name"));

            ingredientQuantity.setText(
                    String.valueOf(getIntent().getDoubleExtra("quantity", 0)));

            ingredientExpiry.setText(getIntent().getStringExtra("expiry"));

            String unit = getIntent().getStringExtra("unit");

            for (int i = 0; i < ingredientUnit.getCount(); i++) {
                if (ingredientUnit.getItemAtPosition(i).toString().equals(unit)) {
                    ingredientUnit.setSelection(i);
                    break;
                }
            }
            Button deleteButton = findViewById(R.id.buttonDeleteIngredient);

            if (ingredientId != -1) {
                deleteButton.setVisibility(View.VISIBLE);
            }

            deleteButton.setOnClickListener(e -> {

                new AlertDialog.Builder(EditAddIngredient.this)
                        .setTitle("Delete ingredient")
                        .setMessage("Delete this ingredient?")
                        .setPositiveButton("Delete", (dialog, which) -> {

                            int result = database.deleteIngredient(ingredientId);

                            if (result > 0) {
                                Toast.makeText(EditAddIngredient.this,
                                        "Ingredient deleted",
                                        Toast.LENGTH_SHORT).show();

                                finish();
                            } else {
                                Toast.makeText(EditAddIngredient.this,
                                        "Could not delete ingredient",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

            saveButton.setText("Update Ingredient");
        }

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

        boolean saved;

        if (ingredientId == -1) {
            long result = database.addIngredient(name, quantity, unit, expiry);
            saved = result != -1;
        } else {
            int result = database.updateIngredient(
                    ingredientId, name, quantity, unit, expiry);
            saved = result > 0;
        }

        if (saved) {
            Toast.makeText(this, "Ingredient saved",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ingredient could not be saved",
                    Toast.LENGTH_SHORT).show();
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
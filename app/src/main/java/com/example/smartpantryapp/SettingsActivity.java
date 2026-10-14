package com.example.smartpantryapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CheckBox largeText = findViewById(R.id.checkLargeText);
        Button backButton = findViewById(R.id.buttonBack);

        SharedPreferences preferences =
                getSharedPreferences("settings", MODE_PRIVATE);

        largeText.setChecked(
                preferences.getBoolean("largeText", false));

        largeText.setOnCheckedChangeListener((button, checked) -> {
            preferences.edit()
                    .putBoolean("largeText", checked)
                    .apply();
        });

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

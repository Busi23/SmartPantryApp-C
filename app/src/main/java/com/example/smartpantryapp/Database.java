package com.example.smartpantryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;import android.database.Cursor;
import java.util.ArrayList;


public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartpantry.db";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =
                "CREATE TABLE ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "quantity REAL NOT NULL CHECK(quantity > 0), " +
                        "unit TEXT NOT NULL, " +
                        "expiry_date TEXT)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database migration steps will go here when needed.
    }
    public long addIngredient(String name, double quantity,
                              String unit, String expiryDate) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);

        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            values.putNull("expiry_date");
        } else {
            values.put("expiry_date", expiryDate);
        }

        return db.insert("ingredients", null, values);}
        public ArrayList<Ingredients> getIngredients() {
            ArrayList<Ingredients> ingredients = new ArrayList<>();

            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.rawQuery(
                    "SELECT id, name, quantity, unit, expiry_date " +
                            "FROM ingredients ORDER BY name", null);

            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    double quantity = cursor.getDouble(2);
                    String unit = cursor.getString(3);
                    String expiryDate = cursor.getString(4);

                    Ingredients ingredient = new Ingredients(
                            id, name, quantity, unit, expiryDate);

                    ingredients.add(ingredient);
                }
            } finally {
                cursor.close();
            }

            return ingredients;
        }
    public int updateIngredient(int id, String name, double quantity,
                                String unit, String expiryDate) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);

        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            values.putNull("expiry_date");
        } else {
            values.put("expiry_date", expiryDate);
        }

        return db.update("ingredients", values,
                "id = ?", new String[]{String.valueOf(id)});
    }
    public int deleteIngredient(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete("ingredients", "id = ?",
                new String[]{String.valueOf(id)});
    }
    }

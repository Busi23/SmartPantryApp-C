package com.example.smartpantryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;import android.database.Cursor;
import java.util.ArrayList;


public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartpantry.db";
    private static final int DATABASE_VERSION = 2;

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
        createRecipeTables(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database migration steps will go here when needed.

            if (oldVersion < 2) {
                createRecipeTables(db);
            }
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
    private void createRecipeTables(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "steps TEXT NOT NULL)");

        db.execSQL(
                "CREATE TABLE recipe_ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "FOREIGN KEY(recipe_id) REFERENCES recipes(id))");

        addSampleRecipe(db);
    }

    private void addSampleRecipe(SQLiteDatabase db) {

        ContentValues recipe = new ContentValues();
        recipe.put("name", "Scrambled Eggs");
        recipe.put("steps",
                "1. Beat the eggs.\n" +
                        "2. Melt the butter in a pan over low heat.\n" +
                        "3. Add the eggs and stir until cooked.");

        long recipeId = db.insertOrThrow("recipes", null, recipe);

        addRecipeIngredient(db, recipeId, "egg", 2, "items");
        addRecipeIngredient(db, recipeId, "butter", 10, "g");
    }

    private void addRecipeIngredient(SQLiteDatabase db, long recipeId,
                                     String name, double quantity, String unit) {

        ContentValues ingredient = new ContentValues();
        ingredient.put("recipe_id", recipeId);
        ingredient.put("name", name);
        ingredient.put("quantity", quantity);
        ingredient.put("unit", unit);

        db.insertOrThrow("recipe_ingredients", null, ingredient);
    }
    public ArrayList<Recipe> getRecipes() {

        ArrayList<Recipe> recipes = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, name, steps FROM recipes ORDER BY name", null);

        try {
            while (cursor.moveToNext()) {

                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String steps = cursor.getString(2);

                Recipe recipe = new Recipe(id, name, steps);
                recipes.add(recipe);
            }
        } finally {
            cursor.close();
        }

        return recipes;
    }
    private String normaliseName(String name) {
        String cleaned = name.trim().toLowerCase(java.util.Locale.ROOT);

        if (cleaned.equals("eggs")) {
            return "egg";
        }

        if (cleaned.equals("tomatoes")) {
            return "tomato";
        }

        if (cleaned.equals("potatoes")) {
            return "potato";
        }

        return cleaned;
    }
    private double baseQuantity(double quantity, String unit) {

        if (unit.equals("kg") || unit.equals("l")) {
            return quantity * 1000;
        }

        return quantity;
    }

    private String baseUnit(String unit) {

        if (unit.equals("kg")) {
            return "g";
        }

        if (unit.equals("l")) {
            return "ml";
        }

        return unit;
    }
    public boolean canMakeRecipe(int recipeId) {

        ArrayList<Ingredients> pantry = getIngredients();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT name, quantity, unit FROM recipe_ingredients " +
                        "WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeId)});

        try {
            if (cursor.getCount() == 0) {
                return false;
            }

            while (cursor.moveToNext()) {

                String requiredName = normaliseName(cursor.getString(0));
                double requiredQuantity = cursor.getDouble(1);
                String requiredUnit = cursor.getString(2);

                double available = 0;

                for (Ingredients ingredient : pantry) {

                    boolean sameName = normaliseName(ingredient.name)
                            .equals(requiredName);

                    boolean sameUnit = baseUnit(ingredient.unit)
                            .equals(baseUnit(requiredUnit));

                    if (sameName && sameUnit) {
                        available += baseQuantity(
                                ingredient.quantity, ingredient.unit);
                    }
                }

                double needed = baseQuantity(
                        requiredQuantity, requiredUnit);

                if (available < needed) {
                    return false;
                }
            }

            return true;

        } finally {
            cursor.close();
        }
    }
    public ArrayList<Recipe> getSuggestedRecipes() {

        ArrayList<Recipe> suggestions = new ArrayList<>();

        for (Recipe recipe : getRecipes()) {
            if (canMakeRecipe(recipe.id)) {
                suggestions.add(recipe);
            }
        }

        return suggestions;
    }
    }

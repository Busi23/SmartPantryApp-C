package com.example.smartpantryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;import android.database.Cursor;
import java.util.ArrayList;


public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartpantry.db";
    private static final int DATABASE_VERSION = 3;

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
        addMoreRecipes(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (oldVersion < 2) {
                createRecipeTables(db);
            }
            if (oldVersion < 3) {
            addMoreRecipes(db);
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

    private void addMoreRecipes(SQLiteDatabase db) {

        addRecipe(db, "Boiled Eggs",
                "1. Place eggs in water.\n2. Boil until cooked.\n3. Cool and peel.",
                new String[]{"egg"},
                new double[]{2},
                new String[]{"items"});

        addRecipe(db, "Cheese Omelette",
                "1. Beat eggs.\n2. Cook in a pan.\n3. Add cheese and fold.",
                new String[]{"egg", "cheese"},
                new double[]{2, 30},
                new String[]{"items", "g"});

        addRecipe(db, "French Toast",
                "1. Beat the egg.\n2. Dip bread in the egg.\n3. Fry both sides.",
                new String[]{"egg", "bread"},
                new double[]{1, 2},
                new String[]{"items", "slices"});

        addRecipe(db, "Pancakes",
                "1. Mix flour, egg and milk.\n2. Make a smooth batter.\n3. Fry small portions.",
                new String[]{"flour", "egg", "milk"},
                new double[]{100, 1, 150},
                new String[]{"g", "items", "ml"});

        addRecipe(db, "Tomato Pasta",
                "1. Cook pasta.\n2. Cook chopped tomatoes.\n3. Mix together.",
                new String[]{"pasta", "tomato"},
                new double[]{100, 2},
                new String[]{"g", "items"});

        addRecipe(db, "Cheese Sandwich",
                "1. Place cheese between bread slices.\n2. Toast until golden.",
                new String[]{"bread", "cheese"},
                new double[]{2, 30},
                new String[]{"slices", "g"});

        addRecipe(db, "Tomato Sandwich",
                "1. Slice the tomato.\n2. Place it on the bread.\n3. Serve.",
                new String[]{"bread", "tomato"},
                new double[]{2, 1},
                new String[]{"slices", "items"});

        addRecipe(db, "Mashed Potatoes",
                "1. Peel and boil potatoes until soft.\n2. Mash thoroughly.\n3. Add butter and mix.",
                new String[]{"potato", "butter"},
                new double[]{3, 15},
                new String[]{"items", "g"});

        addRecipe(db, "Fried Potatoes",
                "1. Slice potatoes.\n2. Heat oil in a pan.\n3. Fry until golden and cooked.",
                new String[]{"potato", "oil"},
                new double[]{2, 15},
                new String[]{"items", "ml"});

        addRecipe(db, "Tomato Omelette",
                "1. Beat the eggs.\n2. Add chopped tomato.\n3. Cook in a pan until set.",
                new String[]{"egg", "tomato"},
                new double[]{2, 1},
                new String[]{"items", "items"});

        addRecipe(db, "Garlic Butter Pasta",
                "1. Cook pasta.\n2. Melt butter and add garlic.\n3. Mix with pasta.",
                new String[]{"pasta", "butter", "garlic"},
                new double[]{100, 15, 1},
                new String[]{"g", "g", "cloves"});

        addRecipe(db, "Banana Pancakes",
                "1. Mash the banana.\n2. Mix with egg and flour.\n3. Cook small pancakes in a pan.",
                new String[]{"banana", "egg", "flour"},
                new double[]{1, 1, 50},
                new String[]{"items", "items", "g"});

        addRecipe(db, "Potato and Egg Hash",
                "1. Dice the potatoes.\n2. Fry until tender.\n3. Add egg and cook thoroughly.",
                new String[]{"potato", "egg", "oil"},
                new double[]{2, 2, 10},
                new String[]{"items", "items", "ml"});

        addRecipe(db, "Cheesy Tomato Pasta",
                "1. Cook pasta.\n2. Prepare a tomato sauce.\n3. Add cheese and stir.",
                new String[]{"pasta", "tomato", "cheese"},
                new double[]{100, 2, 30},
                new String[]{"g", "items", "g"});
    }

    private void addRecipe(SQLiteDatabase db, String name,
                           String steps, String[] names,
                           double[] quantities, String[] units) {

        ContentValues recipe = new ContentValues();
        recipe.put("name", name);
        recipe.put("steps", steps);

        long recipeId = db.insertOrThrow("recipes", null, recipe);

        for (int i = 0; i < names.length; i++) {
            addRecipeIngredient(db, recipeId,
                    names[i], quantities[i], units[i]);
        }
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
    public String getRecipeIngredients(int recipeId) {

        String ingredients = "";
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT name, quantity, unit FROM recipe_ingredients " +
                        "WHERE recipe_id = ? ORDER BY id",
                new String[]{String.valueOf(recipeId)});

        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                double quantity = cursor.getDouble(1);
                String unit = cursor.getString(2);

                ingredients = ingredients + name + " — "
                        + quantity + " " + unit + "\n";
            }
        } finally {
            cursor.close();
        }

        return ingredients;
    }
    }

package com.example.smartpantryapp;

public class Ingredients {

        int id;
        String name;
        double quantity;
        String unit;
        String expiryDate;

        public Ingredients(int id, String name, double quantity,
                           String unit, String expiryDate) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
            this.expiryDate = expiryDate;
        }
    }


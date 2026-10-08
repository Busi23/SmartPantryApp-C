package com.example.smartpantryapp;



public class Recipe {

    int id;
    String name;
    String steps;

    public Recipe(int id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }

    @Override
    public String toString() {
        return name;
    }
}

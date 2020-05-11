package com.colman.fit_me.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.colman.fit_me.utils.Converters;
import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Entity(tableName = "recipe_table")
public class Recipe implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "imgURL")
    public String imgURL;
    @ColumnInfo(name = "category")
    public String category;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "directions")
    public String directions;
    @ColumnInfo(name = "ingredientsJson")
    public String ingredientsJson;
    @ColumnInfo(name = "timestamp")
    @TypeConverters({Converters.class})
    public Date timestamp;

    public Recipe(@NonNull String id, String name, String imgURL, String category, String description, String directions, String ingredientsJson, Date timestamp) {
        this.id = id;
        this.name = name;
        this.imgURL = imgURL;
        this.category = category;
        this.description = description;
        this.directions = directions;
        this.ingredientsJson = ingredientsJson;
        this.timestamp = timestamp;
    }

    public Recipe() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Recipe(Map<String, Object> data) {
        this.id = ((String) data.get("id"));
        this.name = ((String) data.get("name"));
        this.imgURL = ((String) data.get("imgURL"));
        this.category = ((String) data.get("category"));
        this.description = ((String) data.get("description"));
        this.directions = ((String) data.get("directions"));
        this.ingredientsJson = ((String) data.get("ingredientsJson"));
        this.timestamp = timestamp_to_date((Timestamp) data.get("timestamp"));
    }
/*
    public Recipe(String name) {
        this.name = name;
    }
    public Recipe(String name, String imgURL) {
        this.name = name;
        this.imgURL = imgURL;
    }*/


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getIngredientsJson() {
        return ingredientsJson;
    }

    public void setIngredientsJson(String ingredientsJson) {
        this.ingredientsJson = ingredientsJson;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date timestamp_to_date(Timestamp ts){
        return ts.toDate();
    }


}
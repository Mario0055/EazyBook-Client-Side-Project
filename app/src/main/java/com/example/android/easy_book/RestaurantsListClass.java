package com.example.android.easy_book;

public class RestaurantsListClass {


    String Name;
    String Location;
    String Category1;

    public RestaurantsListClass(String name, String location, String category1, String category2, String category3, String imageURL) {
        Name = name;
        Location = location;
        Category1 = category1;
        Category2 = category2;
        Category3 = category3;
        ImageURL = imageURL;
    }

    String Category2;
    String Category3;
    String ImageURL;



    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getCategory1() {
        return Category1;
    }

    public void setCategory1(String category1) {
        Category1 = category1;
    }

    public String getCategory2() {
        return Category2;
    }

    public void setCategory2(String category2) {
        Category2 = category2;
    }

    public String getCategory3() {
        return Category3;
    }

    public void setCategory3(String category3) {
        Category3 = category3;
    }

}


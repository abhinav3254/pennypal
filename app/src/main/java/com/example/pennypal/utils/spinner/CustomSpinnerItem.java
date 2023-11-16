package com.example.pennypal.utils.spinner;

public class CustomSpinnerItem {
    private int imageResource;
    private String itemName;

    public CustomSpinnerItem(int imageResource, String itemName) {
        this.imageResource = imageResource;
        this.itemName = itemName;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getItemName() {
        return itemName;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}


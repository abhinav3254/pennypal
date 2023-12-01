package com.example.pennypal.utils.spinner;



/**
 *
 * @author abhinavkumar
 * Represents an item used within a custom spinner.
 * Contains an image resource and an associated item name.
 */
public class CustomSpinnerItem {
    // Private fields for image resource and item name
    private int imageResource;
    private String itemName;

    /**
     * Constructor to initialize the CustomSpinnerItem.
     * @param imageResource The resource ID of the image for the item.
     * @param itemName      The name of the item.
     */
    public CustomSpinnerItem(int imageResource, String itemName) {
        this.imageResource = imageResource;
        this.itemName = itemName;
    }

    /**
     * Getter method for retrieving the image resource of the item.
     * @return The image resource ID.
     */
    public int getImageResource() {
        return imageResource;
    }

    /**
     * Getter method for retrieving the name of the item.
     * @return The item name.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Setter method to set the image resource of the item.
     * @param imageResource The resource ID of the image for the item.
     */
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    /**
     * Setter method to set the name of the item.
     * @param itemName The name of the item.
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}

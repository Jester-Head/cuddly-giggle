package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Representation of an inventory product which is comprised of parts.
 */
public class Product {

    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * @param id    Automatically generated product ID.
     * @param name  The name of the product entered by the user.
     * @param price The price of the product entered by the user.
     * @param stock The current number of products entered by the user.
     * @param min   The minimum number of allowed products entered by the user.
     * @param max   The maximum number of allowed products entered by the user.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


    /**
     * Gets the product ID.
     *
     * @return the product's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the product's ID.
     *
     * @param id automatically generated product id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the product's name.
     *
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product's name.
     *
     * @param name the product name entered by the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the product's price.
     *
     * @return price the price of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the product's price.
     *
     * @param price the price of the product entered by the user
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the number of products stock.
     *
     * @return stock the number of products in the inventory
     */
    public int getStock() {
        return stock;
    }


    /**
     * Sets the number of products stock.
     *
     * @param stock the number of products in stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets the minimum number of products in stock.
     *
     * @return the minimum amount of products in stock
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the minimum number of products in stock.
     *
     * @param min the min amount of products
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Gets the maximum number of products in stock.
     *
     * @return the maximum amount of products in stock
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the max amount of products.
     *
     * @param max the max amount of products entered by the user
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Get a list of associated parts.
     *
     * @return the list of a product's associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
     * Adds an associated part.
     *
     * @param part part selected by the user
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add( part );
    }

    /**
     * Removes an associated part.
     *
     * @param part a part selected by the user
     * @return true if associated part was deleted,else return false
     */
    public boolean deleteAssociatePart(Part part) {
        return associatedParts.remove( part );
    }

}
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A master list of all parts and products.
 */
public class Inventory {

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static int partId;
    private static int productId;

    /**
     * Default Constructor.
     */
    public Inventory() {
    }

    /**
     * Gets a list of all inventory parts.
     *
     * @return returns a list of all parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Gets a list of all inventory products.
     *
     * @return returns a list of all products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }


    /**
     * Adds part to list of inventory parts.
     *
     * @param newPart Part
     */
    public static void addPart(Part newPart) {

        allParts.add( newPart );
    }

    /**
     * Gets product ID.
     *
     * @return productId
     */
    public static int getProductId() {

        return productId;
    }

    /**
     * Gets part ID.
     *
     * @return productId
     */
    public static int getPartId() {
        return partId;
    }


    /**
     * Gets the size of the parts list and increments it by 1.
     */
    public static void setPartId() {
        partId = allParts.size();
        partId++;
    }

    /**
     * Gets the size of the product list and increments it by 1.
     */
    public static void setProductId() {
        productId = allProducts.size();
        productId++;
    }


    /**
     * Adds a product the list of inventory items.
     *
     * @param newProduct Product created by user
     */
    public static void addProduct(Product newProduct) {
        allProducts.add( newProduct );

    }

    /**
     * Look up a part by ID.
     *
     * @param partId int value entered in search text field
     * @return Part associated with ID
     */
    public static Part lookupPart(int partId) {

        if (!allParts.isEmpty()) {
            for (Part part : allParts) {
                if (part.getId() == partId) {
                    return part;
                }
            }
        }
        return null;
    }

    /**
     * Look up a product by ID.
     *
     * @param productId int value entered in search text field
     * @return Product associated with the ID,else null
     */
    public static Product lookupProduct(int productId) {
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }

        }
        return null;
    }

    /**
     * Look up a product by ID.
     *
     * @param partName String value entered in search text field
     * @return ObservableList of parts with names containing characters entered in the search field
     */
    public static ObservableList<Part> lookupPart(String partName) {

        ObservableList<Part> tempParts = FXCollections.observableArrayList();
        try {
            for (Part part : allParts) {
                if (part.getName().contains( partName )) {
                    tempParts.add( part );
                }
            }
            return tempParts;
        } catch (NumberFormatException numberFormatException) {
            return null;
        }

    }

    /**
     * Look up a product by name.
     *
     * @param productName String value entered in search text field
     * @return list of products with names containing characters entered in search field
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> tempProducts = FXCollections.observableArrayList();
        try {
            for (Product product : allProducts) {
                if (product.getName().contains( productName )) {
                    tempProducts.add( product );
                }
            }
            return tempProducts;
        } catch (NumberFormatException numberFormatException) {
            return null;
        }
    }

    /**
     * Replaces an existing part in the inventory with a modified part.
     *
     * @param index        index of item to be modified in list
     * @param selectedPart item to be modified in list of inventory parts
     */
    public static void updatePart(int index, Part selectedPart) {
        int i = -1;
        for (Part part : allParts) {
            i++;
            if (part.getId() == index) {
                allParts.set( i, selectedPart );
            }
        }
    }

    /**
     * Replaces an existing product in the inventory with a modified product.
     *
     * @param index      index of item to be modified in list
     * @param newProduct item to be modified list of inventory products
     */
    public static void updateProduct(int index, Product newProduct) {
        int i = -1;
        for (Product product : allProducts) {
            i++;
            if (product.getId() == index) {
                allProducts.set( i, newProduct );
            }
        }
    }


    /**
     * Remove a part from the inventory.
     *
     * @param selectedPart item to be deleted in the list of inventory parts
     * @return true if part was deleted,else returns false
     */
    public static boolean deletePart(Part selectedPart) {

        return allParts.remove( selectedPart );
    }


    /**
     * Remove a product from the inventory.
     *
     * @param selectedProduct item to be deleted from the list of inventory products
     * @return true if product was deleted,else returns false
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove( selectedProduct );
    }
}
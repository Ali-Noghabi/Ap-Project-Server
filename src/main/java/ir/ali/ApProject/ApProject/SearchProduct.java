package ir.ali.ApProject.ApProject;

import java.util.ArrayList;

public class SearchProduct {


    public static ArrayList<Product> searchStarProducts(ArrayList<Product> products, String searchSubject) {

        ArrayList<Product> starProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.buyerID.equals("") && (product.subject.contains(searchSubject)
                    || product.description.contains(searchSubject))) {
                if (product.isStar == true)
                    starProducts.add(product);
            }
        }
        return starProducts;
    }

    public static ArrayList<Product> searchNonStarProducts(ArrayList<Product> products, String searchSubject) {

        ArrayList<Product> nonStarProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.buyerID.equals("") && (product.subject.contains(searchSubject)
                    || product.description.contains(searchSubject))) {
                if (product.isStar == false)
                    nonStarProducts.add(product);
            }
        }
        return nonStarProducts;
    }

    public static ArrayList<Product> searchStarProByCategory(ArrayList<Product> products, String category, String searchSubject) {

        ArrayList<Product> starProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.category.equals(category)) {
                if (product.buyerID == null && (product.subject.contains(searchSubject)
                        || product.description.contains(searchSubject))) {
                    if (product.isStar == true)
                        starProducts.add(product);
                }
            }
        }
        return starProducts;
    }


    public static ArrayList<Product> searchNonStarProByCategory(ArrayList<Product> products, String category, String searchSubject) {

        ArrayList<Product> nonStarProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.category.equals(category)) {
                if (product.buyerID == null && (product.subject.contains(searchSubject)
                        || product.description.contains(searchSubject))) {
                    if (product.isStar == false)
                        nonStarProducts.add(product);
                }
            }
        }
        return nonStarProducts;
    }
////here
    

}
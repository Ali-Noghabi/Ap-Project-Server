package ir.ali.ApProject.ApProject;

import java.util.ArrayList;

public class SearchProduct {


    public static ArrayList<Product> searchStarProducts(ArrayList<Product> products,String searchSubject){

        ArrayList<Product> starProducts=new ArrayList<>();
        for(Product product:products){
           if(product.buyerID!=null && product.subject.contains(searchSubject)){
              if(product.isStar==true)
                 starProducts.add(product);
           }
        }
        return starProducts;
    }




}
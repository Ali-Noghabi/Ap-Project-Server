package ir.ali.ApProject.ApProject;

public class Product {
    private static int n = 1;
    public int ID;
    public String category;
    public String subject;
    public String description;
    public String price;
    public Boolean isStar;
    public String photoLink;
    public String sellerID;
    public String buyerID;

    public Product() {
        //set ID
        n++;
        ID = n;
    }
    public Product(boolean lol)
    {

    }


}

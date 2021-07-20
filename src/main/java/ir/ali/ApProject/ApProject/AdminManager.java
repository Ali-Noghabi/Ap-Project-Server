package ir.ali.ApProject.ApProject;


import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class AdminManager {

    public String  findBestSeller(ArrayList<User>  allUsers,ArrayList<Product> allProducts ){

        HashMap<String,Integer> unSortedSellers=new HashMap<>();

        for(Product product:allProducts){   ///create unsortedHashmap
            if(product.buyerID!=null)
            {
                for(User user:allUsers){
                    if(product.sellerID.equals(user.email)){
                        Integer count = unSortedSellers.containsKey(product.sellerID) ? unSortedSellers.get(product.sellerID) : 0;
                        unSortedSellers.put(product.sellerID, count+1);

                    }
                }
            }
        }

        HashMap<String, Integer> sortedSellers    ///sort the hashmap by value
                = unSortedSellers.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Entry.comparingByValue()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        Map.Entry<String,Integer> entry = sortedSellers.entrySet().iterator().next();
        String key = entry.getKey();

        return key;

    }


}

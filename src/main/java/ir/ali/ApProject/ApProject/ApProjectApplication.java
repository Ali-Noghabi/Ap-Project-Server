package ir.ali.ApProject.ApProject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ir.ali.ApProject.ApProject.FirstPage.LoginInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.juli.logging.Log;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;


@SpringBootApplication
@RestController
public class ApProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApProjectApplication.class, args);
    }

    //add New user to database / signUp
    @PostMapping("/postUser")
    public static String PostUser(@RequestBody String name) {
        User user = null;
        try {
            user = new Gson().fromJson(name,
                    new TypeToken<User>() {
                    }.getType()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean isExist = false;
        Select select = new Select();
        for (User tempUser: select.selectAllUsers()) {
            if(tempUser.email.equals(user.email))
                isExist = true;
        }
        Insert tempInsert = new Insert();
        JsonObject obj = new JsonObject();
        if(isExist == false) {
            obj.addProperty("username" , user.email);
            obj.addProperty("token" , user.token);
            obj.addProperty("code" , "200");
            tempInsert.insertUser(user);
        }
        else
        {
            obj.addProperty("code" , "404");
            obj.addProperty("msg" , "username is already taken");
        }
        return obj.toString();
    }

    //get list of all users
    @GetMapping("/getUsers")
    public String GetUsers() {
        Select select = new Select();
        return new Gson().toJson(select.selectAllUsersSafe());
    }

    //add new Product
    @PostMapping("/postProduct")
    public static Product PostProduct(@RequestBody String name) throws Exception {


        Product product = null;
        try {
            product = new Gson().fromJson(name,
                    new TypeToken<Product>() {
                    }.getType()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        Select tempSelect = new Select();
        User Seller = new User(true);
        ArrayList<User> SellersList = tempSelect.selectAllUsers();
        for (User tempUser : SellersList) {
            if (tempUser.token.equals(product.sellerToken)) {
                Seller = tempUser;

            }

        }
        if (Seller.email == null) {
            throw new Exception("403 Token not provided");
        }
        product.sellerID = Seller.email;
        Insert tempInsert = new Insert();
        tempInsert.insertProduct(product);
        return product;
    }

    //get list of all products
    @GetMapping("/getProducts")
    public String GetProducts() {
        Select select = new Select();
        return new Gson().toJson(select.selectAllProducts());
    }

    //Login
    @PostMapping("/login")
    public String Login(@RequestBody String name) {
        LoginInfo loginInfo = null;
        try {
            loginInfo = new Gson().fromJson(name,
                    new TypeToken<LoginInfo>() {
                    }.getType()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        String Token = "";
        JsonObject ret = new JsonObject();
        Select select = new Select();
        boolean userFindFlag = false;
        for (User tempUser : select.selectAllUsers()) {
            if (tempUser.email.equals(loginInfo.email) && tempUser.password.equals(loginInfo.password)) {

                //update token with new random
                tempUser.createToken();
                Token = tempUser.token;

                //login counter++
                Edit tempEdit = new Edit();
                tempEdit.addLoginCounter(tempUser.email);

                ret.addProperty("username", tempUser.email);
                ret.addProperty("token", Token);
                ret.addProperty("code", 200);
                userFindFlag = true;
            }

        }
        if (userFindFlag == false) {
            ret.addProperty("code", 404);
            ret.addProperty("msg", "user or password is incorrect");

        }
        return ret.toString();
    }

    //search query as String between products (include category)
    @PostMapping("/search")
    public String Search(@RequestBody String input) {
        Select tempSelect = new Select();
        JsonObject obj = new JsonParser().parse(input).getAsJsonObject();
        String category = obj.get("category").getAsString();
        String query = obj.get("query").getAsString();
        ArrayList<Product> searchResult = new ArrayList<>();
        if (category.equals("-")) {
            searchResult.addAll(SearchProduct.searchStarProducts(tempSelect.selectAllProducts(), query));
            searchResult.addAll(SearchProduct.searchNonStarProducts(tempSelect.selectAllProducts(), query));
        } else {
            searchResult.addAll(SearchProduct.searchStarProByCategory(tempSelect.selectAllProducts(), category, query));
            searchResult.addAll(SearchProduct.searchNonStarProByCategory(tempSelect.selectAllProducts(), category, query));
        }
        return new Gson().toJson(searchResult);
    }

    //buy product (set buyerID for product)
    @PostMapping("/buy")
    public String Buy(@RequestBody String input) throws Exception {
        JsonObject obj = new JsonParser().parse(input).getAsJsonObject();
        User buyer = new User(true);
        Select tempSelect = new Select();
        ArrayList<User> SellersList = tempSelect.selectAllUsers();
        JsonObject ret = new JsonObject();
        for (User tempUser : SellersList) {
            if (obj.get("buyerToken").getAsString().equals(tempUser.token)) {
                buyer = tempUser;
                ret.addProperty("buyerID", buyer.email);
                ret.addProperty("buyerToken", buyer.token);
                ret.addProperty("productID", obj.get("productID").getAsInt());
                ret.addProperty("code", 200);
            }
        }
        if (buyer.email == null) {
            ret.addProperty("code", 404);
            ret.addProperty("msg", "buyer Not found");
            throw new Exception("403 Token not provided");
        }
        Edit tempEdit = new Edit();
        tempEdit.editBuyerID(buyer.email, obj.get("productID").getAsInt());
        return ret.toString();
    }

    //Admin Manager : get list of all products name
    @GetMapping("/AM/getProducts")
    public String GetProductsName() {
        Select select = new Select();
        ArrayList<String> ret = new ArrayList<>();
        for (Product tempProduct: select.selectAllProducts()) {
            ret.add(tempProduct.subject);
        }

        return new Gson().toJson(ret);
    }

    //Admin Manager : get list of all Sellers Name
    @GetMapping("/AM/getSellers")
    public String GetSellersName() {
        Select select = new Select();
        ArrayList<String> ret = new ArrayList<>();
        for (Product tempProduct:   select.selectAllProducts()) {
            if(ret.contains(tempProduct.sellerID)== false)
                ret.add(tempProduct.sellerID);
        }
        return new Gson().toJson(ret);
    }

    //Admin Manager : get Hashmap of users And loginCounter
    @GetMapping("/AM/getLoginCounter")
    public String GetLoginCounter() {
        Select select = new Select();
        HashMap<String , Integer> ret = new HashMap<>();
        for (User tempUser : select.selectAllUsersSafe()) {
            ret.put(tempUser.email , tempUser.loginCounter);
        }
        return new Gson().toJson(ret);
    }

    //Admin Manager : get Hashmap of users And sold items counter
    @GetMapping("/AM/getBestSeller")
    public String GetBestSeller() {
        Select select = new Select();
        HashMap<String , Integer> ret = new HashMap<>();
        AdminManager adminManagerTemp = new AdminManager();
        ret = adminManagerTemp.findBestSeller(select.selectAllUsersSafe(), select.selectAllProducts());
        return new Gson().toJson(ret);
    }

    //test Spring Boot
    /*
    @GetMapping("/salam")
    public String salam() {
		User testUser = new User("email@email.xyz" , "test" , "test!@#" , "+98TEST");
        Product testProduct = new Product();
        testProduct.setInfo("a", "b", "c", true, "e");
        testProduct.sellerToken = "ss";
		LoginInfo testLogin = new LoginInfo("aliabdollahina@gmail.com" , "ali123ali" , "00:01");
        String json = new Gson().toJson(testProduct);
        return json;
    }
    */
}

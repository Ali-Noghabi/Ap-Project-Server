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


@SpringBootApplication
@RestController
public class ApProjectApplication {

    public static void main(String[] args) {

        SpringApplication.run(ApProjectApplication.class, args);

    }

    //add New user to database
    @PostMapping("/postUser")
    public static User PostUser(@RequestBody String name) {
        User user = null;
        try {
            user = new Gson().fromJson(name,
                    new TypeToken<User>() {
                    }.getType()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        Insert tempInsert = new Insert();
        tempInsert.insertUser(user);
        return user;
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
        System.out.println(product.sellerToken);
        ArrayList<User> SellersList = tempSelect.selectAllUsers();
        for (User tempUser : SellersList) {
            System.out.println(tempUser.token);
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
                Token = tempUser.token;
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

    //login result
//	@GetMapping("/login")
//	public Boolean GetLoginRes(){return loginStatus;}

    @GetMapping("/salam")
    public String salam() {
//		User testUser = new User("email@email.xyz" , "test" , "test!@#" , "+98TEST");
        Product testProduct = new Product();
        testProduct.setInfo("a", "b", "c", true, "e");
        testProduct.sellerToken = "ss";
//		LoginInfo testLogin = new LoginInfo("aliabdollahina@gmail.com" , "ali123ali" , "00:01");
        String json = new Gson().toJson(testProduct);
        return json;
    }

    @PostMapping("/search")
    public String Search(@RequestBody String input) {
        Select tempSelect = new Select();
        JsonObject obj = new JsonParser().parse(input).getAsJsonObject();
        String resultAsJson = "";
        String category = obj.get("category").getAsString();
        String query = obj.get("query").getAsString();
        System.out.println("|" + category + "|" + "    " + query);
        ArrayList<Product> searchResult = new ArrayList<>();
        if (category.equals("-")) {
            System.out.println("im hereeeeeeeeeeeeeeeee");
            searchResult.addAll(SearchProduct.searchStarProducts(tempSelect.selectAllProducts(), query));
            searchResult.addAll(SearchProduct.searchNonStarProducts(tempSelect.selectAllProducts(), query));
        } else {
            System.out.println("lool");
            searchResult.addAll(SearchProduct.searchStarProByCategory(tempSelect.selectAllProducts(), category, query));
            searchResult.addAll(SearchProduct.searchNonStarProByCategory(tempSelect.selectAllProducts(), category, query));
        }
        return new Gson().toJson(searchResult);
    }

}

//	@RequestMapping(value = "/" ,method = RequestMethod.GET)
//	public String home()
//	{
//		return "salam";
//	}
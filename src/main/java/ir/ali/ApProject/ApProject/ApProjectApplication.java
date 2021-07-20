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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


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
        for (User tempUser : select.selectAllUsers()) {
            if (tempUser.email.equals(user.email))
                isExist = true;
        }
        Insert tempInsert = new Insert();
        JsonObject obj = new JsonObject();
        if (isExist == false) {
            obj.addProperty("username", user.email);
            obj.addProperty("token", user.token);
            obj.addProperty("code", "200");
            tempInsert.insertUser(user);
        } else {
            obj.addProperty("code", "404");
            obj.addProperty("msg", "username is already taken");
        }
        return obj.toString();
    }

    @PostMapping("/forgetPass")
    public static String Forget(@RequestBody String input)
    {
        JsonObject obj = new JsonParser().parse(input).getAsJsonObject();
        String username = obj.get("username").getAsString();
        String recPass = "USER NOT FOUND IN DATABASE";
        Select select = new Select();

        for (User tempUser: select.selectAllUsers()) {
            if(tempUser.email.equals(username))
                recPass = tempUser.password;
        }
        // Recipient's email ID needs to be mentioned.
        String to = username;

        // Sender's email ID needs to be mentioned
        String from = "lucifer-team@aryakvn.ir";

        // Assuming you are sending email from through gmails smtp
        String host = "mail.aryakvn.ir";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.enable", "false");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("lucifer-team@aryakvn.ir", "qweqsxqaz123");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        JsonObject ret = new JsonObject();
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Password Recovery");

            // Now set the actual message
            if(recPass.equals("USER NOT FOUND IN DATABASE") == false)
                message.setText("your password is : " + recPass + " \nplease change it");
            else
                message.setText(recPass);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            ret.addProperty("code" , 200);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            ret.addProperty("code" , 405);
        }

        return ret.toString();

    }
    //add New user to database / signUp
    @PostMapping("/deleteProduct")
    public static boolean DeleteProduct(@RequestBody int ID) {
        Delete delete = new Delete();
        delete.deleteProduct(ID);
        return true;
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

        if (product.ID == -1)
            product.ID = Product.n++;

        System.out.println(product);
        Select tempSelect = new Select();
        User Seller = new User(true);
        JsonObject ret = new JsonObject();
        ArrayList<User> SellersList = tempSelect.selectAllUsers();
        for (User tempUser : SellersList) {
            if (tempUser.token.equals(product.sellerToken)) {
                Seller = tempUser;
//                ret.addProperty("code" , 200);
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
    @GetMapping("/getProductsAdmin")
    public String GetProducts() {
        Select select = new Select();
        var ret = select.selectAllProducts();
        return new Gson().toJson(ret);
    }

    //get list of all products
    @GetMapping("/getProducts2")
    public String GetProducts2() {
        Select select = new Select();
        var ret = select.selectAllProducts2();
        var ret2 = new ArrayList<JsonObject>();
        for (JsonObject tempPro : ret) {
            if (tempPro.get("buyerID").getAsString().equals(""))
                ret2.add(tempPro);
        }
        return new Gson().toJson(ret2);
    }

    //get list of all products
    @PostMapping("/getProducts")
    public String GetProducts(@RequestBody String UserName) {
        UserName = UserName.replace("\"" , "");
        System.out.println("username : " + UserName + "wants to watch products");
        Select select = new Select();
        var ret = select.selectAllProducts();
        var ret2 = new ArrayList<Product>();
        for (Product tempPro : ret) {
            if (tempPro.buyerID.equals("") && tempPro.sellerID.equals(UserName))
                ret2.add(tempPro);
        }
        return new Gson().toJson(ret2);
    }

    @PostMapping("/changeStar")
    public boolean changeIsStar(@RequestBody String input) {
        Edit edit = new Edit();
        JsonObject obj = new JsonParser().parse(input).getAsJsonObject();
        edit.editProductStar(obj.get("isStar").getAsBoolean() , obj.get("ID").getAsInt());

        return obj.get("isStar").getAsBoolean();
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
                ret.addProperty("name", tempUser.name);
                ret.addProperty("phonenum", tempUser.phoneNum);
                userFindFlag = true;
            }

        }
        if (userFindFlag == false) {
            ret.addProperty("code", 404);
            ret.addProperty("msg", "user or password is incorrect");

        }
        return ret.toString();
    }

    //EditProfile
    @PostMapping("/editUser")
    public String editUser(@RequestBody String input) {
        JsonObject obj = new JsonParser().parse(input).getAsJsonObject();

        JsonObject ret = new JsonObject();
        Select select = new Select();
        boolean userFindFlag = false;
        for (User tempUser : select.selectAllUsers()) {
            if (tempUser.token.equals(obj.get("token").getAsString())) {

                Edit edit = new Edit();
                System.out.println(obj.get("name").getAsString() + " " + obj.get("password").getAsString() + " " +
                        obj.get("phonenumber").getAsString() + " " + tempUser.email);
                edit.editUserInfo(obj.get("name").getAsString(), obj.get("password").getAsString(),
                        obj.get("phonenumber").getAsString(), tempUser.email);

                ret.addProperty("username", tempUser.email);
                ret.addProperty("code", 200);
                userFindFlag = true;
            }

        }
        if (userFindFlag == false) {
            ret.addProperty("code", 404);
            ret.addProperty("msg", "Login token expired");

        }
        return ret.toString();
    }

    //EditProfile
    @PostMapping("/editPro")
    public String editPro(@RequestBody String input) {
        JsonObject obj = new JsonParser().parse(input).getAsJsonObject();

        JsonObject ret = new JsonObject();

        Edit edit = new Edit();

        System.out.println("Someone just tried to edit product");
        edit.editProduct(obj.get("subject").getAsString(), obj.get("description").getAsString(),
                obj.get("price").getAsString(), obj.get("image").getAsString(), obj.get("ID").getAsInt());

        ret.addProperty("code", 200);

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
        searchResult.addAll(SearchProduct.searchStarProducts(tempSelect.selectAllProducts(), query));
        searchResult.addAll(SearchProduct.searchNonStarProducts(tempSelect.selectAllProducts(), query));
        return new Gson().toJson(searchResult);
    }

    //search query as String between products (include category)
    @PostMapping("/searchEmail")
    public String SearchEmail(@RequestBody String input) {
        Select tempSelect = new Select();
        JsonObject obj = new JsonParser().parse(input).getAsJsonObject();
        String query = obj.get("query").getAsString();
        ArrayList<User> searchResult = tempSelect.selectAllUsers();
        JsonObject ret = new JsonObject();
        for (User tempUser:searchResult) {
            if(tempUser.email.equals(query))
                ret.addProperty("Phone" , tempUser.phoneNum);
        }
        return ret.toString();
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
                System.out.println("User " + buyer.email + " buy product with ID = " + obj.get("productID").getAsInt());
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
        for (Product tempProduct : select.selectAllProducts()) {
            ret.add(tempProduct.subject);
        }

        return new Gson().toJson(ret);
    }

    //Admin Manager : get list of all Sellers Name
    @GetMapping("/AM/getSellers")
    public String GetSellersName() {
        Select select = new Select();
        ArrayList<String> ret = new ArrayList<>();
        for (Product tempProduct : select.selectAllProducts()) {
            if (ret.contains(tempProduct.sellerID) == false)
                ret.add(tempProduct.sellerID);
        }
        return new Gson().toJson(ret);
    }

    //Admin Manager : get Hashmap of users And loginCounter
    @GetMapping("/AM/getLoginCounter")
    public String GetLoginCounter() {
        Select select = new Select();
        HashMap<String, Integer> ret = new HashMap<>();
        for (User tempUser : select.selectAllUsersSafe()) {
            ret.put(tempUser.email, tempUser.loginCounter);
        }
        return new Gson().toJson(ret);
    }

    //Admin Manager : get Hashmap of users And sold items counter
    @GetMapping("/AM/getBestSeller")
    public String GetBestSeller() {
        Select select = new Select();
        AdminManager adminManagerTemp = new AdminManager();
        JsonObject ret = new JsonObject();
        ret.addProperty("best" ,adminManagerTemp.findBestSeller(select.selectAllUsersSafe(), select.selectAllProducts()));
        return ret.toString();
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

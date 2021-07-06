package ir.ali.ApProject.ApProject;

import com.google.gson.JsonObject;
import ir.ali.ApProject.ApProject.FirstPage.LoginInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.juli.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;


@SpringBootApplication
@RestController
public class ApProjectApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApProjectApplication.class, args);

	}
	//add New user to database
	@PostMapping("/postUser")
	public static User PostUser(@RequestBody String name)
	{
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
	public String GetUsers()
	{
		Select select = new Select();
		return new Gson().toJson(select.selectAllUsersSafe());
	}

	//add new Product
	@PostMapping("/postProduct")
	public static Product PostProduct(@RequestBody String name)
	{
		Select tempSelect = new Select();
		tempSelect.selectAllUsers();
		Product product = null;
		try {
			product = new Gson().fromJson(name,
					new TypeToken<Product>() {
					}.getType()
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Insert tempInsert = new Insert();
		tempInsert.insertProduct(product);
		return product;
	}

	//get list of all products
	@GetMapping("/getProducts")
	public String GetProducts()
	{
		Select select = new Select();
		return select.selectAllProducts();
	}

	//Login
	@PostMapping("/login")
	public String Login(@RequestBody String name)
	{
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
		for (User tempUser:select.selectAllUsers()){
			if(tempUser.email.equals(loginInfo.email) && tempUser.password.equals(loginInfo.password))
			{
				Token = tempUser.token;
				ret.addProperty("username" , tempUser.email);
				ret.addProperty("token" , Token);
				ret.addProperty("code" , 200);
				userFindFlag = true;
			}

		}
		if(userFindFlag == false)
		{
			ret.addProperty("code" , 404);
			ret.addProperty("msg" , "user or password is incorrect");

		}
		return ret.toString();
	}

	//login result
//	@GetMapping("/login")
//	public Boolean GetLoginRes(){return loginStatus;}

	@GetMapping("/salam")
	public String salam() {
//		User testUser = new User("email@email.xyz" , "test" , "test!@#" , "+98TEST");
//		Product testProduct = new Product();
//		testProduct.setInfo("a" , "b" , "c" , true, "e");
		LoginInfo testLogin = new LoginInfo("aliabdollahina@gmail.com" , "ali123ali" , "00:01");
		String json = new Gson().toJson(testLogin);
		return json;
	}
}

//	@RequestMapping(value = "/" ,method = RequestMethod.GET)
//	public String home()
//	{
//		return "salam";
//	}
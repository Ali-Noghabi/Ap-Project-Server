package ir.ali.ApProject.ApProject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class ApProjectApplication {
	public static void main(String[] args) {

		SpringApplication.run(ApProjectApplication.class, args);
//		Connect connect = new Connect();
//		System.out.println(connect);

	}
	public static void addProductToDataBase(String Json)
	{



	}
	@GetMapping("/getProducts")
	public String GetProducts()
	{
		Select select = new Select();
		return select.selectAllProducts();
	}
	@GetMapping("/getUsers")
	public String GetUsers()
	{
		Select select = new Select();
		return select.selectAllUsers();
	}

	@PostMapping("/postProduct")
	public static Product PostProduct(@RequestBody String name)
	{
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
	@GetMapping("/salam")
	public String salam() {
		User testUser = new User("email@email.xyz" , "test" , "test!@#" , "+98TEST");
		Product testProduct = new Product();
		testProduct.setInfo("a" , "b" , "c" , true, "e");
		String json = new Gson().toJson(testProduct);
		return json;
	}
}

//	@RequestMapping(value = "/" ,method = RequestMethod.GET)
//	public String home()
//	{
//		return "salam";
//	}
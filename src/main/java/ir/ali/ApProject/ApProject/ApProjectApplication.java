package ir.ali.ApProject.ApProject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

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
	@GetMapping("/get")
	public String GetUser()
	{
//		ArrayList<Product> products = new ArrayList<>();
//		Product A = new Product();
//		A.setCategory("CAR");
//		A.setInfo("Benz 2012" , "Brand New \n Only 200km Worked \n White" , "6000"  , true , "NoPhoto");
//		products.add(A);
//		Product B = new Product();
//		B.setCategory("LAPTOP");
//		B.setInfo("Asus K20" , "Brand New \n i7 9100k \n GTX 3080" , "1200"  , false , "NoPhoto");
////		products.add(B);
//		String Json = new Gson().toJson(products);
//
//		return Json;
		Select select = new Select();
		return select.selectAll();
	}

	@PostMapping("/get")
	public static Product PostProducts(@RequestBody String name)
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
		Insert tempProduct = new Insert();
		tempProduct.insertProduct(product);
		return product;
	}
}
//@GetMapping("/salam")
//	public String salam(@RequestParam(value = "name", defaultValue = "World") String name) {
//		return String.format("Hello %s!", name);
//	}
//	@RequestMapping(value = "/" ,method = RequestMethod.GET)
//	public String home()
//	{
//		return "salam";
//	}
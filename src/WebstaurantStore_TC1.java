import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.annotations.AfterTest;

public class WebstaurantStore_TC1 {
	
	WebDriver driver = new ChromeDriver();
	
	@BeforeTest 
	public void setup() {			
		driver.get("https://www.webstaurantstore.com/");	
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);	       
    }
	
	@Test
	public void verifyLandingPageLoaded() {
		
		String expectedTitle = "WebstaurantStore: Restaurant Supplies & Foodservice Equipment";
		String actualTitle = driver.getTitle();
		Assert.assertEquals(actualTitle, expectedTitle);
		
	}
	
	@Test(dependsOnMethods= {"verifyLandingPageLoaded"})
	public void verifySearchResults() {
		driver.findElement(By.cssSelector("input[id='searchval']")).sendKeys("stainless work table");
		driver.findElement(By.cssSelector("button[value='Search']")).click();
		
		List<WebElement> searchResults = driver.findElements(By.xpath("//div[@class='ag-item gtm-product ']"));
		List<WebElement> resultsWithTable = new ArrayList<WebElement>();
		for(WebElement element : searchResults) {
	        if (element.getText().contains("Table")) {
	            resultsWithTable.add(element);
	        }
		}
		Assert.assertEquals(searchResults, resultsWithTable);
	}
	
	@Test(dependsOnMethods= {"verifySearchResults"})
	public void addToCartAndEmpty() throws InterruptedException {
		List<WebElement> addToCart =driver.findElements(By.xpath("//*[contains(@value,'Add to Cart')]"));
		WebElement lastButton = addToCart.get(addToCart.size()-1);
		lastButton.click();
		driver.findElement(By.cssSelector("button[class='close']")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("a[data-testid='cart-nav-link']")).click();
		driver.findElement(By.cssSelector("a[class^='emptyCartButton']")).click();
		driver.findElement(By.cssSelector("button[class='btn btn-primary']")).click();
		
		Thread.sleep(1000);
		String cartEmpty = driver.findElement(By.cssSelector("p[class='header-1']")).getText();
		String expectedCartEmpty = "Your cart is empty.";
		Assert.assertEquals(cartEmpty, expectedCartEmpty);
	}
	
	@AfterTest
	public void TeardownTest() {
        driver.quit();
    } 

}

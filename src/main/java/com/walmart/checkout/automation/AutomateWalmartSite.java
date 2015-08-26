package com.walmart.checkout.automation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


/**
 * Implementation on automating an end to end user e-commerce transaction flow using any open source tool for walmart website
 * with an existing customer on chrome browser.
 * 
 * @author mselvam
 * 
 */
public class AutomateWalmartSite {
	private WebDriver driver;
	private String username;
	private String password;
	private String url;
	private String firstName;
	private String lastName;
	private String email;
	private List<String> searchItems;
	private SoftAssert softAssert ;
	//constants
	private static final String PROPNAME = "input.properties";
	private static final String DELIMITER = ",";
	private static final long SLEEPTIME = 7000;
	private static final String ONEITEM = "(1 item)";

	@BeforeTest
	public void doBeforeTest() {
		System.out.println("Before Test");
		softAssert = new SoftAssert();
		searchItems = new ArrayList<String>();
		readInputData();
	}

	/*
	 * step 1: Launch the given walmart url step 2: Perform a search on home
	 * page from a pool of key words given below
	 */

	@Test
	public void testCheckOutPage() throws Exception {
		WebElement searchBox;
		
			for (int i = 0; i < searchItems.size(); i++) {
				driver = new ChromeDriver();
				Assert.assertNotNull(driver);
				driver.get(url);
				driver.manage().window().maximize();
				Thread.sleep(SLEEPTIME);
				searchBox = driver.findElement(By.name("query"));
				Assert.assertTrue(validateElement(searchBox));
				searchBox.sendKeys(searchItems.get(i));
				System.out.println("Now searching for: "
						+ searchBox.getAttribute("value"));
				searchBox.submit();
				Thread.sleep(SLEEPTIME);
				addItemToCart(); // method call to add item to cart
				Thread.sleep(SLEEPTIME);
			}

		}
	

	/*
	 * step 3: Add the item to cart and then login using existing account which
	 * is set up with at least one shipping address step 4. Validate that item
	 * added is present in the cart and is the only item in the cart
	 */

	private void addItemToCart() throws InterruptedException {
		try{
		WebElement redyToSelect;
		WebElement addTOCartBtn;
		WebElement layout1;
		WebElement headerCart;
		WebElement itemCount = null;
		WebElement cartChkOutBtn;
		WebElement shipToHome;
		WebElement shipToButton1;
		WebElement shipToButton2;
		WebElement paymentPage;
		WebElement cartCheckOutBck;
		WebElement removeBtn;
		String selectedItemId;
		
		System.out.println("Looking for an item to do checkout");
		redyToSelect = getXpathElement(".//*[contains(@class, 'js-product-title')]");
		//redyToSelect = getXpathElement(".//*[@id='tile-container']/div[1]/div[1]/div[1]/h4/a[@class='js-product-title']");
		Assert.assertNotNull(redyToSelect, "Item not found for checkout");
		Assert.assertTrue(validateElement(redyToSelect),
				"Item not enabled for checkout");
		System.out.println(redyToSelect.getText());
		selectedItemId = redyToSelect.getText();
		redyToSelect.click();
		Assert.assertNotNull(selectedItemId, "Selected ItemId is null");
		Thread.sleep(SLEEPTIME);

		System.out.println("Add item to Cart");
		addTOCartBtn = getXpathElement(".//*[@id='WMItemAddToCartBtn']");
		Assert.assertNotNull(addTOCartBtn, "CartButton is null");
		Assert.assertTrue(validateElement(addTOCartBtn),
				"CartButton not enabled");
		addTOCartBtn.click();
		Thread.sleep(SLEEPTIME);

		System.out.println("Closing the layout1 Window to proceed with sing in");
		try{
		layout1 = getXpathElement(".//*[contains(@class, 'Modal-closeButton hide-content display-block-m js-modal-close')]");
		softAssert.assertNotNull(layout1, "layout close button not found");
		softAssert.assertTrue(validateElement(layout1),"layout close button not enabled");
		layout1.click();
		Thread.sleep(SLEEPTIME);
		}catch(NoSuchElementException nse){
			revealLowerPrice();			
		}	

		// Entering into Signin/Login
		singInToAccount();
		System.out.println("Sucessfully Logged in");
		Thread.sleep(SLEEPTIME);

		System.out.println("Click on Cart Button after logged in");
		headerCart = getXpathElement(".//*[contains(@class, 'header-cart')]/a/i[@class='wmicon wmicon-cart']");
		Assert.assertNotNull(headerCart, "Cart Button button not found");
		Assert.assertTrue(validateElement(headerCart),"Cart Button button not enabled");
		headerCart.click();
		Thread.sleep(SLEEPTIME);

		System.out.println("Finding the number of Items in the current cart");
		itemCount = getXpathElement(".//*[@data-automation-id='pos-item-quantity']");
		Assert.assertNotNull(itemCount, "itemCount data is not found");
		Assert.assertTrue(validateElement(itemCount),"itemCount data is not enabled");
		Thread.sleep(SLEEPTIME);
		
		Assert.assertTrue(validateCheckout(itemCount,selectedItemId));
		
		System.out.println("Continue Checkout");
		cartChkOutBtn = getXpathElement(".//*[@data-automation-id='checkout']");
		Assert.assertNotNull(cartChkOutBtn, "Cart Button button not found");
		Assert.assertTrue(validateElement(cartChkOutBtn),"Cart Button button not enabled");
		cartChkOutBtn.click();
		Thread.sleep(SLEEPTIME);
		
		//check the S2H is selected
		System.out.println("Check the shipment type");
		shipToHome = getXpathElement(".//*[@id='COAC1ShpOptBasicVALUE0Btn']");
		Assert.assertNotNull(shipToHome, "Ship to home option not found");
		Assert.assertTrue(validateElement(shipToHome),"Ship to home option not enabled");
		Thread.sleep(SLEEPTIME);
		
		System.out.println("Click ship confimration");
		shipToButton1 = getXpathElement(".//*[@id='COAC1ShpOptContBtn']");
		Assert.assertNotNull(shipToButton1, "Ship button1 not found");
		Assert.assertTrue(validateElement(shipToButton1),"Ship button1 not enabled");
		shipToButton1.click();
		Thread.sleep(SLEEPTIME);
		
		System.out.println("Click ship confimration...Continue");
		shipToButton2 = getXpathElement(".//*[@id='COAC2ShpAddrContBtn']");
		Assert.assertNotNull(shipToButton2, "Ship button2 not found");
		Assert.assertTrue(validateElement(shipToButton2),"Ship button2 not enabled");
		shipToButton2.click();
		Thread.sleep(SLEEPTIME);
		
		System.out.println("Check you are on payment page");
		paymentPage = getXpathElement(".//*[@data-view-name='payment']");
		Assert.assertNotNull(paymentPage, "Payment Page not found");
		Assert.assertTrue(validateElement(paymentPage),"Payment Page not enabled");
		paymentPage.click();
		Thread.sleep(SLEEPTIME);

		System.out.println("Returning to checkout page");
		cartCheckOutBck = getXpathElement(".//*[contains(@class, 'checkout-header-cart')]");
		Assert.assertNotNull(cartCheckOutBck, "Return to check out button not found");
		Assert.assertTrue(validateElement(cartCheckOutBck),"Return to check out button not enabled");
		cartCheckOutBck.click();
		Thread.sleep(SLEEPTIME);
		
			
		System.out.println("Remove the item from check out");
		removeBtn = getXpathElement(".//*[@id='CartRemItemBtn']");
		Assert.assertNotNull(removeBtn, "Return to check out button not found");
		Assert.assertTrue(validateElement(removeBtn),"Return to check out button not enabled");
		removeBtn.click();
		Thread.sleep(SLEEPTIME);
					
		System.out.println("Sign out from System");							
		signOutToAccount();
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		}

	

	/*
	 * login using existing account which is set up with at least one shipping
	 * address
	 */
	private void singInToAccount() throws InterruptedException {
		WebElement singIn;
		WebElement userNameElement;
		WebElement passwordElement;
		WebElement singinButton;

		System.out.println("Click on Singin link");
		singIn = getXpathElement(".//*[contains(@class, 'header-account-signin')]");
		Assert.assertNotNull(singIn, "Singin link not found");
		Assert.assertTrue(validateElement(singIn), "Singin link not enabled");
		singIn.click();
		Thread.sleep(SLEEPTIME);

		System.out.println("Sigin Page Entering User name");
		userNameElement = getXpathElement(".//*[@id='login-username']");
		Assert.assertNotNull(userNameElement, "Username text box not found");
		Assert.assertTrue(validateElement(userNameElement),
				"Username text box not enabled");
		userNameElement.sendKeys(username);

		System.out.println("Sigin Page Entering Password");
		passwordElement = getXpathElement(".//*[@id='login-password']");
		Assert.assertNotNull(passwordElement, "Password text box not found");
		Assert.assertTrue(validateElement(passwordElement),
				"Password text box not enabled");
		passwordElement.sendKeys(password);

		Thread.sleep(SLEEPTIME);
		System.out.println("Click on Singin Button");
		singinButton = getXpathElement(".//*[@data-automation-id='login-sign-in']");
		Assert.assertNotNull(singinButton, "Singin Button not found");
		Assert.assertTrue(validateElement(singinButton),
				"Singin Button not enabled");
		singinButton.click();
		Thread.sleep(SLEEPTIME);

	}

	private void signOutToAccount() throws InterruptedException {
		WebElement signOut;
		WebElement myAccount;
		Thread.sleep(SLEEPTIME);
		System.out.println("Click on MyAccount button");
		myAccount = getXpathElement(".//*[contains(@class, 'dropdown-link js-flyout-toggle flyout-toggle')]");
		Assert.assertNotNull(myAccount, "MyAccount button not found");
		Assert.assertTrue(validateElement(myAccount), "MyAccount button not enabled");
		Actions builder = new Actions(driver);
		builder.moveToElement(myAccount).build().perform();
		//WebDriverWait wait = new WebDriverWait(driver, 15);
		Thread.sleep(SLEEPTIME);
		
		System.out.println("Click on Signout link");
		//signOut = getXpathElement(".//*[contains(@class, 'js-flyout-modal flyout-modal header-flyout-modal')]/ul/span/li/a[@class='js-sign-out']");
		//myAccount = getXpathElement(".//*[contains(@class, 'js-flyout-modal flyout-modal header-flyout-modal')]/ul/span/li");
		signOut = driver.findElement(By.xpath(".//*[contains(@class, 'js-sign-out')]"));
		if(null != signOut)
		signOut.click();		

	}
	
	/**
	 * This method validates the checked item and its count
	 */
	 
	private boolean validateCheckout(WebElement itemCount,String selectedItemId) throws InterruptedException{
		WebElement checkoutItem;
		String checkoutItemId;
		System.out.println("Check whether only one item is added to the item");
		Assert.assertEquals(itemCount.getText(), ONEITEM,"Checkout Item Count is more than one");		
		System.out.println("Compare the selected Item and checked out item is same");
		checkoutItem = getXpathElement(".//*[contains(@class, 'cart-item-name js-product-title')]/a/span");
		checkoutItemId = checkoutItem.getText();		
		Assert.assertEquals(checkoutItemId, selectedItemId,"Wrong item checked out");
		Thread.sleep(SLEEPTIME);
		return true;
	}

	private WebElement getXpathElement(String xPathString) throws NoSuchElementException, InterruptedException
			  {
		WebElement webElem = null;
		try{
		webElem = driver.findElement(By.xpath(xPathString));
		}catch(NoSuchElementException nse){
			while (webElem == null && SLEEPTIME <= 14000) {
				Thread.sleep(SLEEPTIME);
				webElem = driver.findElement(By.xpath(xPathString));
			}
		}
		return webElem;
	}

	private boolean validateElement(WebElement we) {
		Assert.assertNotNull(we,
				"Given WebElement is null inside validateElement");
		if (null != we && (we.isEnabled()|| we.isDisplayed() || we.isSelected())) {
			return true;
		}
		return false;
	}
	
	private void revealLowerPrice() throws NoSuchElementException, InterruptedException{
		System.out.println("Enter your name and email to reveal this low price");
		WebElement firstNameElement;
		WebElement lastNameElement;
		WebElement emailElement;
		WebElement layout1;
		WebElement layout2;
		
		System.out.println("Entering First Name");
		firstNameElement = getXpathElement(".//*[@data-automation-id='proxy-login-first-name']");
		Assert.assertNotNull(firstNameElement, "first name text box not found");
		Assert.assertTrue(validateElement(firstNameElement),
				"first name text box not enabled");
		firstNameElement.sendKeys(firstName);

		System.out.println("Entering Last Name");
		lastNameElement = getXpathElement(".//*[@data-automation-id='proxy-login-last-name']");
		Assert.assertNotNull(lastNameElement, "last name text box not found");
		Assert.assertTrue(validateElement(lastNameElement),
				"last name text box not enabled");
		lastNameElement.sendKeys(lastName);
		
		System.out.println("Entering email");
		emailElement = getXpathElement(".//*[@data-automation-id='proxy-login-email']");
		Assert.assertNotNull(emailElement, "email text box not found");
		Assert.assertTrue(validateElement(emailElement),
				"email text box not enabled");
		emailElement.sendKeys(email);
		Thread.sleep(SLEEPTIME);
		
		System.out.println("Click continue button");
		layout2 = getXpathElement(".//*[contains(@class, 'modal-alert-actions hide-content display-block-m')]/button[@class='btn js-modal-agree btn-primary modal-alert-btn-primary']");
		softAssert.assertNotNull(layout2, "continue button not found");
		softAssert.assertTrue(validateElement(layout2),	"continue button not enabled");
		layout2.click();
		Thread.sleep(SLEEPTIME);
		
		layout1 = getXpathElement(".//*[contains(@class, 'Modal-closeButton hide-content display-block-m js-modal-close')]");
		softAssert.assertNotNull(layout1, "layout close button not found");
		softAssert.assertTrue(validateElement(layout1),"layout close button not enabled");
		layout1.click();
		Thread.sleep(SLEEPTIME);
		
	}

	/*
	 * Reading the URL and list of items from property file if you need to
	 * change any you just need to change in the property file Assumption:
	 * itemlist is seperated by comma(,)
	 */
	private void readInputData() {
		System.out.println("Inside reaadInputData");
		String[] listOfItems = null;
		Properties prop = new Properties();
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(PROPNAME);
		try {
			if (null != inputStream) {
				prop.load(inputStream);
				if (null != prop) {
					url = prop.getProperty("url");
					username = prop.getProperty("username");
					password = prop.getProperty("password");
					firstName = prop.getProperty("firstName");
					lastName = prop.getProperty("lastName");
					email = prop.getProperty("email");
					String itemList = prop.getProperty("itemlist");
					if (null != itemList && !itemList.isEmpty()) {
						itemList = itemList.trim();
						listOfItems = itemList.split(DELIMITER);
						for (String str : listOfItems) {
							searchItems.add(str);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}

}

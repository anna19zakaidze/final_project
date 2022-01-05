import Models.Book;
import Models.BookList;
import Models.User;
import com.google.gson.Gson;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class FinalProjectTests {

    public WebDriver openBrowser(){
        //open chrome browser
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        return driver;
    }
    @Test
    public void Test1(){

        //Add user with API https://bookstore.toolsqa.com/Account/v1/User
        User usr = new User();
        usr.username="Anavyaa";
        usr.password="AnPass012!.";
        Map<String,String> user = new HashMap<String, String>();
        user.put("userName",usr.username);
        user.put("password",usr.password);
        Response response = given().contentType(JSON)
                .body(user).post("https://bookstore.toolsqa.com/Account/v1/User");
        response.print();
        User createdUser= response
                .jsonPath()
                .getObject("", User.class);

        //Go to https://demoqa.com/login
        WebDriver driver = openBrowser();
        String url="https://demoqa.com/login";
        driver.get(url);
        //Login with added account
        WebElement userName = driver.findElement(By.id("userName"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("login"));
        userName.sendKeys(createdUser.username);
        password.sendKeys(usr.password);
        System.out.println(loginBtn.getText());
        loginBtn.click();
        driver.close();
        //Go to https://demoqa.com/profile
        //Click on 'Delete Account'
        url="https://demoqa.com/profile";
        driver.get(url);
        WebElement deleteBtn = driver.findElement(By.id("submit"));
        deleteBtn.click();
        //Validate popup message 'User Deleted.'
        Assert.assertEquals(driver.switchTo().alert().getText(),"User Deleted.");
        //Try to login with deleted account credentials
        url="https://demoqa.com/login";
        driver.get(url);
        userName.sendKeys(createdUser.username);
        password.sendKeys(createdUser.password);
        loginBtn.click();
        //Validate error text 'Invalid username or password!'
        WebElement errorTxt = driver.findElement(By.id("name"));
        Assert.assertEquals(errorTxt,"Invalid username or password!");
        //Call https://bookstore.toolsqa.com/Account/v1/Authorized with deleted credentials
        User usr2 = new User();
        usr2.username=createdUser.username;
        usr2.password=createdUser.password;
        Map<String,String> user2 = new HashMap<String, String>();
        user.put("userName",usr2.username);
        user.put("password",usr2.password);
        Response response2 = given().contentType(JSON)
                .body(user).post("https://bookstore.toolsqa.com/Account/v1/User");
        int responseMessage = response2.getStatusCode();
        Assert.assertEquals(responseMessage,404);
    }

    @Test
    public void Test2(){

        //Go to https://demoqa.com/books
        WebDriver driver = openBrowser();
        String url="https://demoqa.com/books";
        driver.get(url);
        WebElement searchBox = driver.findElement(By.id("searchBox"));

        //Write 'O'Reilly Media' in search textbox
        searchBox.sendKeys("O'Reilly Media");
        driver.close();


        //Call https://bookstore.toolsqa.com/BookStore/v1/Books
        Response response = RestAssured.given().when()
                .get("https://bookstore.toolsqa.com/BookStore/v1/Books");
        BookList booksList= response
                .jsonPath()
                .getObject("", BookList.class);
        List<Book> books = booksList.books;
        List<Book> booksWithPublisherOReillyMedia = books.stream().filter(x->x.publisher.equals("O'Reilly Media")).collect(Collectors.toList());

        //Validate that count of books with publisher 'O'Reilly Media' is equals to returned list size in UI
        int bookCount = booksWithPublisherOReillyMedia.size();
        Assert.assertEquals(bookCount,6);

        //Validate that book with title 'Understanding ECMAScript 6' is the last element in UI and in API
        Book book = books.stream().filter(x->x.title.equals("Understanding ECMAScript 6")).collect(Collectors.toList()).get(0);
        int indexOfBook = books.indexOf(book);
        Assert.assertEquals(indexOfBook,7);
        
    }


}

import Models.Book;
import Models.BookList;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FinalProjectTests {
//    @Test
//    public void Test1(){
//    //Add user with API https://bookstore.toolsqa.com/Account/v1/User
//        RestAssured.baseURI ="https://bookstore.toolsqa.com/Account/v1/User";
//                             "https://bookstore.toolsqa.com/swagger/#/Account/AccountV1UserPost"
//        RequestSpecification request = RestAssured.given();
//        JSONObject requestParams = new JSONObject();
//        requestParams.put("userName","AnnaZak");
//        requestParams.put("password", "AnPass012.");
//        // Add a header stating the Request body is a JSON
//        request.header("Content-Type", "application/json");
//        // Add the Json to the body of the request
//        request.body(requestParams.toJSONString());
//
//        // Post the request and check the response
//        Response response = request.post("");
//        int statusCode = response.getStatusCode();
//        Assert.assertEquals(statusCode, "201");
//        String successCode = response.jsonPath().get("SuccessCode");
//        Assert.assertEquals( "Correct Success code was returned", successCode, "OPERATION_SUCCESS");
//    }

    public WebDriver openBrowser(){
        //open chrome browser
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        return driver;
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

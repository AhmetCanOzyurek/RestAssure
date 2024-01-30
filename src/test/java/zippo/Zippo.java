package zippo;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

public class Zippo {

    @BeforeTest
    public void beforeTest(){
        RestAssured.baseURI = "https://www.zippopotam.us";
    }
    @Test
    public void test1(){
        get("/us/90210")
                .then()
                .statusCode(200)
                .log().body()
                .body("country", equalTo("United States"))
                .body("places[0].state", equalTo("California"))
                .body("'country abbreviation'",equalTo("US"))//bosluk oldgu icin tek tirnak icine aldık
                .body("places[0].'place name'",equalTo("Beverly Hills"))
        ;
    }



    @Test
    public void test2(){
        get("/TR/06000")
                .then()
                .statusCode(200)
                .log().body();
    }
}

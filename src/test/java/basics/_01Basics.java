package basics;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;

public class _01Basics {

    @Test
    public void test() {
        given()                //on veriler, requirmentlar, headers, cookies, body...
                .when()        //yapilan islem  GET, POST, DELETE, PUT
                .then();       //Assertions, statusCode, Json path assertions

        when()
                .get()
                .then()
                ;
    }

    @Test
    public void test02() {
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                //.log().body()
                //.log().all()
                //.log().cookies()
                .log().headers()
                ;
    }

    @Test
    public void test03_statusCode(){
        String url = "https://reqres.in/api/users?page=2";
        given()
                .get(url)
                .then()
                .statusCode(200)  //status code 200 olmali
                ;

    }@Test
    public void test04_ResponseTime(){
        String url = "https://reqres.in/api/users0?page=2";
      long time =  given()
                .get(url)
                .timeIn(TimeUnit.MILLISECONDS)
                ;
        System.out.println(time);

    }
    @Test
    public void test05_parameters(){
        String url = "https://reqres.in/api/users0?page=2";
        given()
                .get(url)
                .then()
                .log().body()
                .statusCode(200)  //status code 200 olmali
        ;

    }
    @Test
    public void test06_parameters(){


        given()
                .pathParams("page",1)
                .pathParams("link","api")
                .get("https://reqres.in/{link}/users?page={page}")
                .then()
                .log().body()
                .statusCode(200)  //status code 200 olmali
        ;
    }
    @Test
    public void test07_baseUri(){
        baseURI = "https://reqres.in";       //baseUrl tanimi icindir.

        given()

                .get("https://reqres.in/api/users?page=1")
                .then()
                .statusCode(200)  //status code 200 olmali
        ;

        /*
        baseURI tanimli ise
                GET, POST, ...... 'da http ya da https yoksa baseURI kullanilir
         */

        given()

                .get("/api/users?page=1")
                .then()
                .statusCode(200)  //status code 200 olmali
        ;
    }

}

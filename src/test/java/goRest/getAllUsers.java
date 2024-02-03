package goRest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class getAllUsers {

    @BeforeTest
    public void beforeTest() {
        RestAssured.baseURI = "https://gorest.co.in/";
    }

    @Test
    public void getAllUsersTest1() {
        given()
                .when()
                .get("/public/v2/users")
                .then()
                .log().body()
                .statusCode(oneOf(200, 201, 204))
        ;
    }

    @Test
    public void getFirstNameTest2() {
//        Object obj = given() / ilk olarak ne dondurdugunu bilmediğim icin Object olarak aldım
        String firstName = given()
                .when()
                .get("/public/v2/users")
                .then()
//                .log().body()
                .statusCode(oneOf(200, 201, 204))
                .extract().path("name[0]");
        //System.out.println(obj);
        System.out.println(firstName);
    }

    @Test
    public void test3_GetAllNamesThenSortThenWriteToConsol() {
        List<String> names = given()
                .when()
                .get("/public/v2/users")
                .then()
                .statusCode(200)
                .extract().path("name");
//        System.out.println(names.stream().sorted().collect(Collectors.toList()));

        Collections.sort(names);
        System.out.println(names);
    }

    @Test
    public void test3_getFemaleNamesThenSortThenWriteToConsol() {
        List<String> names = given()
                .when()
                .get("/public/v2/users")
                .then()
                .body(not(empty()))
                .statusCode(200)
                .extract().path("findAll{it.gender=='female'}.name");
//        System.out.println(names.stream().sorted().collect(Collectors.toList()));

        Collections.sort(names);
        System.out.println(names);

        /*
        findAll{it.gender=='female'}.name
         eger root varsa
         users.findAll
         seklinde kullanılır
        array icinde gender=='female' olanlarin name'lerini return eder
         */
    }

    @Test
    public void test3_getAllNameWithJsonPath() {
        List<String> names = given()
                .when()
                .get("/public/v2/users")
                .then()
                .body(not(empty()))
                .statusCode(200)
                .extract().jsonPath().getJsonObject("name");
//        System.out.println(names.stream().sorted().collect(Collectors.toList()));

        Collections.sort(names);
        System.out.println(names);

    }

    @Test
    public void test4_genel() {

        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://gorest.co.in")
                .build();

        ResponseSpecification resSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(oneOf(200, 201, 204))
                .build();

        given()
                .spec(reqSpec)
                .when()
                .get("/public/v2/users")
                .then()
                .spec(resSpec);


        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/public/v2/users")
                .then()
                .statusCode(oneOf(200, 201, 204))
        ;
    }


}

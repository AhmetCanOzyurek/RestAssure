package goRest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.oneOf;

public class GoRestUserTest {
    RequestSpecification reqSpec;
    ResponseSpecification resSpec;
    int id;

    @BeforeTest
    public void beforeTest() {
        reqSpec = new RequestSpecBuilder()

                .addHeader("Authorization", "Bearer cd2be388382dd25c79fa3bac2b6f31631ed282b8770349e78d629b09d3665d09")
                .setBaseUri("https://gorest.co.in")
                .build();
        resSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(oneOf(200, 201, 204))
                .expectBody(containsString("id"))
                .expectBody(containsString("name"))
                .expectBody(containsString("email"))
                .expectBody(containsString("gender"))
                .expectBody(containsString("status"))
                .build();


    }


    @Test
    public void postAUserTest() {
        String json = getJSonData();

        //gelen json'ı response icine kaydettik

        Response response = given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .spec(resSpec)
                .extract().response();
        id = response.jsonPath().get("id");
        String name = response.jsonPath().get("name");
        String email = response.jsonPath().get("email");
        System.out.println("id = " + id);

       /*
       tek seferlik kullanım icin

       String json = getJSonData();
        int id = given()
                .spec(reqSpec)
                .body(json)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .spec(resSpec)
                .extract().path("id");
        System.out.println("id = " + id);*/
    }


    @Test(dependsOnMethods = "postAUserTest")
    public void updateAUserTest() {
        String json = getJSonData();
        Response res = given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/public/v2/users/" + id)
                .then()
                .log().body()
                .spec(resSpec)
                .extract().response();
        id = res.jsonPath().get("id");
        System.out.println("id = " + id);
    }

    @Test(dependsOnMethods = "postAUserTest")
    public void deleteAUserTest() {
        String json = getJSonData();
        given()
                .spec(reqSpec)
                .body(json)
                .when()
                .delete("/public/v2/users/" + id)
                .then()
                .log().body()
                .statusCode(oneOf(200,201,204))
        ;

    }


    public String getJSonData() {
        String[] genders = {"male", "female"};
        String[] statuses = {"active", "inactive"};
        String name = RandomStringUtils.randomAlphabetic(5, 10);
        String email = RandomStringUtils.randomAlphabetic(5, 10) + "@gmail.com";
        String gender = genders[new Random().nextInt(genders.length)];
        String status = statuses[new Random().nextInt(statuses.length)];

        String data = "  {\n" +
                "        \"name\": \"" + name + "\",\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"gender\": \"" + gender + "\",\n" +
                "        \"status\":\"" + status + "\"}";
        return data;
    }
}

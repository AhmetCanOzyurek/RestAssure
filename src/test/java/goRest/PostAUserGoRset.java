package goRest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.oneOf;

public class PostAUserGoRset {
    RequestSpecification reqSpec;
    @BeforeTest
    public void beforeTest(){
        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer cd2be388382dd25c79fa3bac2b6f31631ed282b8770349e78d629b09d3665d09")
                .setBaseUri("https://gorest.co.in")
                .build();
    }
    @Test
    public void postAUSer() {
        String data = getJSonData();

        int id = given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(data)
                .post("https://gorest.co.in/public/v2/users")
                .then()
                .log().body()
                .statusCode(oneOf(200,201,204))
                .extract().path("id");

        User user = given()
                .spec(reqSpec)
                .get("/public/v2/users/"+id)
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(User.class);

        System.out.println(user);
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

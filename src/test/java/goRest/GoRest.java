package goRest;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.path.json.JsonPath.from;

public class GoRest {

    @Test
    public void getAUSer() {
        get("https://gorest.co.in/public/v2/users/6129464")
                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test
    public void getUserInAClass() {
        User user = get("https://gorest.co.in/public/v2/users/6129464")

                .then()
                .statusCode(200)
                .log().body()
                .extract().as(User.class);
        System.out.println(user);
        System.out.println(user.getEmail());
        System.out.println(user.getName());
    }

    @Test
    public void getAllUserInAClass() {
        List<User> users = get("https://gorest.co.in/public/v2/users")
                .then()
                .statusCode(200)
                .log().body()
                .extract().jsonPath().getList("", User.class);

        for (User user : users) {
            System.out.println(user);
            System.out.println("----------");
        }
    }

    @Test
    public void getResponse() {
        Response response = get("https://gorest.co.in/public/v2/users/6129457")
                .then()
                .statusCode(200)
                .extract().response();
        String name = response.path("name");
        String email = response.path("email");

        System.out.println("name = " + name);
        System.out.println("email = " + email);
    }

    @Test
    public void getResponse2() {
        String response = get("https://gorest.co.in/public/v2/users/6129457")
                .then()
                .statusCode(200)
                .extract().asString();

        String name = from(response).get("name");
        String email = from(response).get("email");

        System.out.println("name = " + name);
        System.out.println("email = " + email);
    }

}

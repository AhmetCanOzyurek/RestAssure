package localJsonServer;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LocalTestDb {

    @Test
    public void  test01_getAllPosts(){
        given()
                .when()
                .get("http://localhost:3000/people/1" )
                .then()
                .log().body()
                .statusCode(200);
    }
}

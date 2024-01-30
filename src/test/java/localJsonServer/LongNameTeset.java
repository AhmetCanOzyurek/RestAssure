package localJsonServer;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class LongNameTeset {

    @BeforeTest
    public void beforeTest() {
        RestAssured.baseURI = "http://localhost:3000";
    }
@Test(invocationCount = 10)
    public void postUsersTest1(){
    String name = RandomStringUtils.randomAlphabetic(0, 10) + " ulrich";


    String data = "  {\n" +
            "        \"name\": \"" + name + "\",\n" +
            "        \"location\": \"New York, UsSA\",\n" +
            "        \"phone\": \"900-544-55362\",\n" +
            "        \"courses\": [\n" +
            "            \"JavaScript Programming\",\n" +
            "            \"Web Development Fundamentals\"\n" +
            "        ]\n" +
            "    }";

    given()
            .contentType(ContentType.JSON)
            .body(data)
            .when()
            .post("/people")
            .then()
            .log().body()
            .statusCode(200)
            ;
}
@Test(priority = 1, dependsOnMethods = "postUsersTest1")
    public void findLongestNameAmongstTheData(){
    List<String> names = given()
                .when()
                .get("/people")
                .then()
                .statusCode(200)
                .extract().path("name")
                ;
    String longestName = "";
    for (int i = 0; i <names.size() ; i++) {
        if(names.get(i).length() > longestName.length())
            longestName = names.get(i);
    }
    System.out.println("longestName = " + longestName);
}

}

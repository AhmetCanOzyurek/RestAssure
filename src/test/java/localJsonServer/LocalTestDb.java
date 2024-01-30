package localJsonServer;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

public class LocalTestDb {
    @BeforeTest
    public void beforeTest() {
        //RestAssured.baseURI = "http://localhost:3000";
    }
    @Test
    public void test01_getAllPosts() {
        given()
                .when()
                .get("/people")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", hasItems(1, 2, 3))
        ;
    }

    //extract a value    @BeforeTest
    //    public void beforeTest() {
    //        RestAssured.baseURI = "http://localhost:3000";
    //    }
    @Test
    public void test02_getAllPosts() {
        List<Integer> id = given()
                .when()
                .get("/people")
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("id")           //extract icerisindeki veriye ulaşmamız icin
                //path istedigimiz verinin yolunu verir
                ;
        System.out.println("id = " + id);
    }

    //hasItem -> tek deger assert
    //hasItems -> array icinde olmasi beklenen degerler
    @Test
    public void localPostTest() {
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


        int id = given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/people")
                .then()
                .log().body()
                .statusCode(200)
                .extract().path("id");
        ;

        System.out.println("kayit id'si : " + id);
        String jsonForUpdate = "  {\n" +
                "        \"name\": \"" + name + "\",\n" +
                "        \"location\": \"New York, UsSA\",\n" +
                "        \"phone\": \"900-544-55362\",\n" +
                "        \"courses\": [\n" +
                "            \"JavaScript \",\n" +
                "            \"Web Development \"\n" +
                "        ]\n" +
                "    }";


        given()
                .contentType(ContentType.JSON)
                .body(jsonForUpdate)
                .when()
                .put("/people/" + id)
                .then()
                .log().body()
                .statusCode(200)
        ;

        given()
                .when()
                .delete("/people/" + id)
                .then()
                .statusCode(404)
        ;

    }


    public static void main(String[] args) {
        System.out.println(getRandomString(20, 30));
    }


    public static String getRandomString(int min, int max) {
        String str = "abcdefABCDEF12345 ";

        String rndStr = "";
        int last = min + new Random().nextInt(max - min);
        for (int i = 0; i < last; i++) {
            rndStr += str.charAt(new Random().nextInt(str.length()));
        }
        return rndStr;
    }




    /*
yeni bir class'da
test1 : post ile random 10 kayit ekleyin
test2 :users'Larin username'leri liste olarak alin
        max uzunluktaki username'i ekrana yazdirin
     */
}
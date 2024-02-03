package zippo;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import zippo.pojoClasse.Location;
import zippo.pojoClasse.Place;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Zippo2 {

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() {
        RestAssured.baseURI = "https://www.zippopotam.us/";
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://www.zippopotam.us/")
                //.setBasePath("/TR")
                .log(LogDetail.URI)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();

    }


    //  https://www.zippopotam.us/TR/06080 adresini get edin

    @Test
    public void test1_getData06080() {
        given()
                .log().uri()
                .when()
                .get("TR/06080")
                .then()
                .spec(responseSpecification)
        ;

    }

//    post  code'un empty olmadigini
    //country'nin turkey oldugunu
    //country æbbreviation'ın TR oldugunu
//places 3.sunun "Sokullu Mah." oldugunu match edin
    // places'in size'nın 18 oldugunu

    @Test
    public void getDataAndAssertionTest() {
        given()
                .when()
                .get("TR/06080")
                .then()
                .spec(responseSpecification)
                .body("'post code'", not(empty()))
                .body("country", equalTo("Turkey"))
                .body("'country abbreviation'", equalTo("TR"))
                .body("places[2].'place name'", equalTo("Sokullu Mah."))
                .body("places", hasSize(18))
                .extract().path("places");
    }

    @Test
    public void test2_getDataAllStatesAreAnkara() {
        // json'daki places'in size'i 18 olur
        given()
                .log().uri()
                .when()
                .get("TR/06080")
                .then()
                .spec(responseSpecification)
                .body("places.findAll{it.state == 'Ankara'}.state", hasSize(18))
                .body("places.findAll{it.state != 'Ankara'}.state", hasSize(0))
        ;
    }

    //TR ve 06080 yerine pathParam kullaniniz
    @Test
    public void test2_getDataUsePathParam() {
        // json'daki places'in size'i 18 olur
        String country = "TR";
        String postCode = "06080";


        given()
                .log().uri()
                .when()
                .pathParams("ulke", country)
                .pathParams("postaKodu", postCode)
                .get("{ulke}/{postaKodu}")
                .then()
                .spec(responseSpecification)
                .body("places.findAll{it.state == 'Ankara'}.state", hasSize(18))
                .body("places.findAll{it.state != 'Ankara'}.state", hasSize(0))
        ;
    }

    //country'yi extract edin ve Turkey oldugunu assert edin
    //3.mahallenin adını extract edin
    @Test
    public void getDataExtractPlaceName() {
        String placeName = given()
                .spec(requestSpecification)
                .when()
                .get("TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().path("places[2].'place name'");
        String country = given()
                .spec(requestSpecification)
                .when()
                .get("TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().path("country");

        Assert.assertEquals(country, "Turkey");
        Assert.assertEquals(placeName, "Sokullu Mah.");
    }

    @Test
    public void getDataExtractPlaceName2() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().response();

        String placeName = response.then()
                .extract().path("places[2].'place name'");
        Assert.assertEquals(placeName, "Sokullu Mah.");

        String country = response.then()
                .extract().jsonPath().get("country");
        Assert.assertEquals(country, "Turkey");

        response.prettyPrint();
    }

    @Test
    public void getDataExtractPlaceNames() {
        Response response = given()
                .spec(requestSpecification)
                .when()
                .get("TR/06080")
                .then()
                .spec(responseSpecification)
                .extract().response();

        List<String> placeNames = response.then()
                .extract().path("places.'place name'");

        Assert.assertEquals(placeNames.size(), 18);

        String longestName = "";
        for (String placeName : placeNames) {
            System.out.println(placeName);
            if (placeName.length() > longestName.length())
                longestName = placeName;
        }
        System.out.println("longestName = " + longestName);
    }

    //  /TR/06080 datasini pojoya map edin
    @Test
    public void getDataToPojo() {

        Response res = given()
                .spec(requestSpecification)
                .when()
                .get("/TR/06080")
                .then()
                //.spec(responseSpecification)
                .extract().response();


        Location location = res.then().extract().as(Location.class);
        if (location.getPlaces() != null) {
            for (Place place : location.getPlaces()) {
                String str = location.getCountry() + "\t" +
                        place.getState() + "\t" +
                        place.getPlaceName();

                System.out.println(str);
            }
        }
    }

    @Test
    public void getDataToPojo1() throws IOException {
        FileWriter fileWriter = new FileWriter(("Places.txt"));
        for (int i = 6095; i < 6100; i++) {
            String postCode = getPostaKodu(i);
            Response res = given()
                    .spec(requestSpecification)
                    .when()
                    .get("/TR/06080")
                    .then()
                    //.spec(responseSpecification)
                    .extract().response();
            Location location = res.then().extract().as(Location.class);
            if (res != null) {
                for (Place place : location.getPlaces()) {
                    String str = location.getCountry() + "\t" +
                            place.getState() + "\n" +
                            place.getPlaceName();
                    fileWriter.write(str);
                }

            }

        }
        fileWriter.close();
    }

    public String getPostaKodu(int num) {
        String code = String.valueOf(num);
        for (int i = code.length(); i < 5; i++) {
            code = "0".concat(code);
        }
        return code;
    }

}



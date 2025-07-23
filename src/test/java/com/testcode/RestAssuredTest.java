package com.testcode;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.Test;

import io.restassured.RestAssured;

public class RestAssuredTest {

    @Test(priority = 1)
    public void testGetPeopleEndpoint() {
        RestAssured
            .given()
                .baseUri("http://localhost:4500")
            .when()
                .get("/people")
            .then()
                .statusCode(200)
                .body("$", not(empty()))
                .body("sno", everyItem(notNullValue()))
                .body("name", everyItem(notNullValue()))
                .body("city", everyItem(notNullValue()));
    }
    

    @Test(priority = 2)
    public void testPostPeopleEndpoint() {
        String requestBody = "{\"id\": \"105\", \"sno\": 105, \"name\": \"John Doe\", \"city\": \"New York\" }";

        RestAssured
            .given()
                .baseUri("http://localhost:4500")
                .header("Content-Type", "application/json")
                .body(requestBody)
            .when()
                .post("/people")
            .then()
                .statusCode(201)
                .body("sno", equalTo(105))
                .body("name", equalTo("John Doe"))
                .body("city", equalTo("New York"));
    }

    @Test(priority = 3)
    public void testUpdatePeopleEndpoint() {
        int snoToUpdate = 105; // Assuming this sno exists after the POST request
        String updatedBody = "{ \"sno\":105,\"name\": \"Jane Doe\", \"city\": \"Los Angeles\" }";

        RestAssured
            .given()
                .baseUri("http://localhost:4500")
                .header("Content-Type", "application/json")
                .body(updatedBody)
            .when()
                .put("/people/" + snoToUpdate)
            .then()
                .statusCode(200)
                .body("sno", equalTo(snoToUpdate))
                .body("name", equalTo("Jane Doe"))
                .body("city", equalTo("Los Angeles"));
    }

    @Test(priority = 4)
    public void testDeletePeopleEndpoint() {
        String snoToDelete = "105";

        RestAssured
            .given()
                .baseUri("http://localhost:4500")
            .when()
                .delete("/people/" + snoToDelete)
            .then()
                .statusCode(200);
    }
}
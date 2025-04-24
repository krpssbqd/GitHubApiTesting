package core;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;

@Slf4j
public class ServiceCaller {

    @Setter(AccessLevel.NONE)
    public Response response;

    public Response get(String path, RequestSpecification requestSpecification) {
        response = given()
                .filter(new AllureRestAssured())
                .log().everything()
                .spec(requestSpecification)
                .get(path);
        log.info("GET Response: {}", response.prettyPrint());
        return response;
    }

    public Response post(String path, Object requestBody, RequestSpecification requestSpecification) {
        response = given()
                .filter(new AllureRestAssured())
                .spec(requestSpecification)
                .log().everything()
                .body(requestBody.toString())
                .post(path);
        log.info("POST Response: {}", response.prettyPrint());
        return response;
    }

    public Response delete(String path, RequestSpecification requestSpecification) {
        response = given()
                .filter(new AllureRestAssured())
                .spec(requestSpecification)
                .log().everything()
                .delete(path);
        log.info("DELETE Response: {}", response.prettyPrint());
        return response;
    }

    public Response put(String path, Object requestBody) {
        response = given()
                .log().everything()
                .body(requestBody.toString())
                .put(path);
        log.info("PUT Response: {}", response.prettyPrint());
        return response;
    }
}

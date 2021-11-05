import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class ApiTest {
    @Test
    public void testPost()
    {
        String content = null;
        try {
            content = Files.lines(Paths.
                            get("src/test/java/jsonTest.txt"))
                    .reduce("", String::concat);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String baseUri = "https://reqres.in/";//"http//:jsonplaceholder.tupicode.con/";
        String postEndPoint = "/api/register";//"/posts";
        String body = "{\n" +
                "   \"email\":\"eve.holt@reqres.in\",\n" +
                "   \"password\": \"pistol\"\n}";
        given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .body(body)
                .log().all()
                .post(postEndPoint)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchema(content));
    }
    @Test
    public void testGetWithout()
    {
        given()
                .baseUri("https://reqres.in/")
                .pathParam("user_id", 23)
                .log().all()
                .get("api/users/{user_id}")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND);
        Response response  = when().get("https://reqres.in/api/users/{user_id}", 2);
        response.then().log().all();


    }
    @Test
    public void testGetWith1()
    {
        Response response  = when().get("https://reqres.in/api/unknown");
        response.then().log().all().statusCode(200);
    }
    @Test
    public void testGetWith2()
    {
        given()
                .baseUri("https://reqres.in/")
                .pathParam("user_id", 2)
                .log().all()
                .get("api/unknown{user_id}")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK);
    }
}

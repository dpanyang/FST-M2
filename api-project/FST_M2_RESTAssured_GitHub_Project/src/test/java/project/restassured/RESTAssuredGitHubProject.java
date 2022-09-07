package project.restassured;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;
import static org.hamcrest.CoreMatchers.equalTo;

public class RESTAssuredGitHubProject {

    // Declare request specification
    RequestSpecification requestSpec;
    // Declare response specification
    ResponseSpecification responseSpec;

    String ssh_Key;
    int id_SSH_Key;

    @BeforeClass
    public void setUp() {
        // Create request specification
        requestSpec = new RequestSpecBuilder()
                // Set content type
                .setContentType(ContentType.JSON)
                .setAuth(oauth2("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"))
                // Set base URL
                .setBaseUri("https://api.github.com/")
                // Build request specification
                .build();

        responseSpec = new ResponseSpecBuilder()
                // Check status code in response
                .expectStatusCode(200)
                // Check response content type
                .expectContentType("application/json")
                // Check if response contains name property
                .expectBody("status", equalTo("alive"))
                // Build response specification
                .build();
    }


    @Test(priority=1)

    public void PostSshKey() {
        String reqBody = "{\"title\": \"TestAPIKey\", \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAgQCAlAa7P8f8XvFZ0XeJUxqmP+R9sCYJpUYO1Up3wwGsY5CyovVdc+oBcLhad70S2hQmOOSqcVasDTgyC5Z9KYdoIJybwJgzZRFWIuEZDEO9uLPDXrm5kRzI89vcR6E7CM/WXL9SfUgcD/CfzBkXRCa6Gf8gMfBpE19E6ChmcvaD3Q==\"}";
        Response response = given().spec(requestSpec) // Use requestSpec
                .body(reqBody) // Send request body
                .when().post("/user/keys"); // Send POST request

        //reqBody = "{\"id\": 77233, \"name\": \"Hansel\", \"status\": \"alive\"}";
        //response = given().spec(requestSpec) // Use requestSpec
                //.body(reqBody) // Send request body
                //.when().post("/user/keys"); // Send POST request

        // Assertions
        //response.then().spec(responseSpec); // Use responseSpec
       response.then().statusCode(201);


        System.out.println("Response Body is =>  "+response.getBody().asString());
        String responseBody  = response.getBody().prettyPrint();
        System.out.println("Response Body is prettyPrint =>  "+responseBody);
        int responseStatus = response.getStatusCode();
        System.out.println("Response status =>  "+responseStatus);
        id_SSH_Key=response.jsonPath().getInt("id");
        System.out.println("Id is  >>>> "+id_SSH_Key);
    }

    @Test(priority=2)

    public void GetSshKey(){

        Response response=given().spec(requestSpec).when().get("/user/keys/"+id_SSH_Key);
        System.out.println("Id is next method >>>> "+id_SSH_Key);
        String responseBody  = response.getBody().prettyPrint();
        int responseStatus = response.getStatusCode();
        System.out.println("Response Body is prettyPrint =>  Get &&& response status"+responseBody + responseStatus);

        response.then().statusCode(200);
    }

    @Test(priority=3)

    public void DeleteSshKey() {
        Response response=given().spec(requestSpec).when().delete("/user/keys/"+id_SSH_Key);

        String responseBody  = response.getBody().prettyPrint();

        int responseStatus=response.getStatusCode();

        System.out.println("responseStatus---" + responseStatus);

        response.then().statusCode(204);


    }


}

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.given;

public class JiraTest {
    public static void main(String[] args) {

// TODO Auto-generated method stub

        RestAssured.baseURI = "http://localhost:8080";

//Login Scenario

        SessionFilter session = new SessionFilter(); // need to extract sessionId instead of parsing Json

//To bypass HTTP validation on the real project, we need to use method relaxedHTTPSValidation() after given()
        //Sometimes RestAssured will not recognize post("/rest/auth/1/session"), and that's why we need to
        //use the method above
        String response = given().relaxedHTTPSValidation().header("Content-Type", "application/json").body("{\r\n" +

                "    \"username\": \"RahulShetty\",\r\n" +

                "    \"password\": \"XXXX11\"\r\n" +

                "}").log().all().filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();

        String expectedMessage = "Hi How are you?";

//Add comment

        String addCommentResponse = given().pathParam("key", "10101").log().all().header("Content-Type", "application/json").body("{\r\n" +

                "    \"body\": \"" + expectedMessage + "\",\r\n" +

                "    \"visibility\": {\r\n" +

                "        \"type\": \"role\",\r\n" +

                "        \"value\": \"Administrators\"\r\n" +

                "    }\r\n" +

                "}").filter(session).when().post("/rest/api/2/issue/{key}/comment").then().log().all()

                .assertThat().statusCode(201).extract().response().asString();

        JsonPath js = new JsonPath(addCommentResponse);

        String commentId = js.getString("id");

//Add Attachment

        given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("key", "10101")

                .header("Content-Type", "multipart/form-data")//use this header for sending attachment

                .multiPart("file", new File("jira.txt")).when().// use multiPart method when we want
                //to send an attachment. Also use File class to send files. In this example we don't provide
                //the whole path, cause it is located in the root of the project, otherwise, we need
                //to specify the path

                        post("rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);

//Get Issue

        String issueDetails = given().filter(session).pathParam("key", "10101")

                .queryParam("fields", "comment")// in order to limit Json response through
                //query Param. If we want to get the response of certain fields

                .log().all().when().get("/rest/api/2/issue/{key}").then()

                .log().all().extract().response().asString();

        System.out.println(issueDetails);

        JsonPath js1 = new JsonPath(issueDetails);

        int commentsCount = js1.getInt("fields.comment.comments.size()");

        for (int i = 0; i < commentsCount; i++) {

            String commentIdIssue = js1.get("fields.comment.comments[" + i + "].id").toString();

            if (commentIdIssue.equalsIgnoreCase(commentId)) {

                String message = js1.get("fields.comment.comments[" + i + "].body").toString();

                System.out.println(message);

                Assert.assertEquals(message, expectedMessage);

            }

        }

    }


}

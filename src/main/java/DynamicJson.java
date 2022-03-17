import files.Payload;
import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class DynamicJson {

    @Test(dataProvider = "BooksData")
    public void addBook(String isbn, String aisle) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        String resp = given().header("Content-Type", "application/json")
                .body(Payload.addBook(isbn, aisle))
                .when()
                .post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
        JsonPath js = ReUsableMethods.rawToJson(resp);
        String id = js.get("ID");
//        System.out.println("Book ID:: " + id);


        String deleteResponse = given().header("Content-Type", "application/json")
                .body(Payload.deleteBook(id))
                .when()
                .post("Library/DeleteBook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();

        System.out.println("Book with ID:: " + id + " " + deleteResponse);

    }


    @DataProvider(name = "BooksData")
    public Object[][] getData() {
        return new Object[][]{
                {"dssqtrdfs", "134231"},
                {"fakrtshdf", "124431"},
                {"fdqftrsdf", "193451"}
        };
    }

}

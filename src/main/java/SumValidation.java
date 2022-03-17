import files.Payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SumValidation {


    /**
     * Verify if Sum of all Course prices matches with Purchase Amount
     * int amount = 900
     */
    @Test
    public void testSumOfCourses() {

        JsonPath js = new JsonPath(Payload.coursePrice());

        int count = js.getInt("courses.size()");
        int amount = js.getInt("dashboard.purchaseAmount");

        int totalAmount = 0;
        int amountOfEachCourse = 0;

        for (int i = 0; i < count; i++) {
            int price = js.getInt("courses[" + i + "].price");
            int copies = js.getInt("courses[" + i + "].copies");
            amountOfEachCourse = price * copies;
            totalAmount += amountOfEachCourse;
        }
        System.out.println("Total amount of all courses:: " + totalAmount);
        Assert.assertEquals(totalAmount, amount, "Total prices do not match");
    }
}


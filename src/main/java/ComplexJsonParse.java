import files.Payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

public class ComplexJsonParse {

    public static void main(String[] args) {

        JsonPath js = new JsonPath(Payload.coursePrice());
        //  1. Print No of courses returned by API

        int count = js.getInt("courses.size()");
        System.out.println(count);

        // 2.Print Purchase Amount

        int amount = js.getInt("dashboard.purchaseAmount");
        System.out.println(amount);

        // 3. Print Title of the first course

        String title = js.getString("courses[0].title");
        System.out.println(title);

        // 4. Print All course titles and their respective Prices

        for (int i = 0; i < count; i++) {
            String courseTitle = js.getString("courses[" + i + "].title");
            int coursePrice = js.getInt("courses[" + i + "].price");
            System.out.println("Title:: " + courseTitle + "Course price::" + coursePrice);
        }

        //5. Print no of copies sold by RPA Course

        int numOfCopiesRpa = 0;
        for (int i = 0; i < count; i++) {
            String courseTitles = js.get("courses[" + i + "].title");
            if (courseTitles.equalsIgnoreCase("RPA")) {
                numOfCopiesRpa = js.getInt("courses[" + i + "].copies");
                System.out.println("Num of copies PRA:: " + numOfCopiesRpa);
                break;
            }
        }
    }
}

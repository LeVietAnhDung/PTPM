package EditDepartment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Initialization.Init;

public class TC005_EditDepartment_ValidUpdates extends Init {

    public static void main(String[] args) throws InterruptedException {

        SetUp("edge");
        driver.get("https://localhost:7222");

        driver.findElement(By.linkText("Departments")).click();
        goToDepartmentsList();

        // Tìm dòng chứa "Khoa Ngoại"
        WebElement row = driver.findElement(
                By.xpath("//table//tr[td[contains(text(),'Khoa Ngoại')]]"));

        row.findElement(By.linkText("Edit")).click();

        driver.findElement(By.xpath("//input[@id='Code']")).sendKeys("D022");
        assertTrue(driver.getCurrentUrl().contains("/Departments/Edit"),
                "Navigate to Edit page");

        // Cập nhật thông tin
        WebElement nameInput = driver.findElement(By.id("Name"));
        nameInput.clear();
        nameInput.sendKeys("Khoa Ngoại Tổng hợp");

        driver.findElement(By.xpath("//button[text()='Save']")).click();

        assertTrue(driver.getCurrentUrl().contains("/Departments"),
                "Return to Departments list after saving");

        // Kiểm tra lại bảng
        WebElement table = driver.findElement(By.tagName("table"));
        String tableText = table.getText();

        assertTrue(tableText.contains("Khoa Ngoại Tổng hợp"),
                "Updated name appears in table");

        Teardown();
    }

    // --------------------- Custom Assert ----------------------

    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            System.out.println("[PASSED] " + message);
        } else {
            System.out.println("[FAILED] " + message);
        }
    }

    // --------------------- Helper -----------------------------

    private static void goToDepartmentsList() {
        try {
            // Nếu table hiển thị rồi thì không cần điều hướng nữa
            driver.findElement(By.tagName("table"));
            System.out.println("[INFO] Department list already displayed.");
        } catch (Exception e) {
            // Nếu chưa vào list thì click lại link
            System.out.println("[INFO] Navigating back to Department list...");
            driver.findElement(By.linkText("Departments")).click();
        }
    }

}

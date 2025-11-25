package CreateDepartment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Initialization.Init;

public class TC004_CreateDepartment_DuplicateCode extends Init {

    public static void main(String[] args) throws InterruptedException {

        SetUp("edge");
        driver.get("https://localhost:7222");

        driver.findElement(By.linkText("Departments")).click();
        driver.findElement(By.linkText("+ New Department")).click();

        driver.findElement(By.id("Code")).sendKeys("D002");   
        driver.findElement(By.id("Name")).sendKeys("Khoa nội");

        WebElement activeCheckbox = driver.findElement(By.id("IsActive"));
        if (!activeCheckbox.isSelected()) {
            activeCheckbox.click();
        }

        driver.findElement(By.xpath("//button[normalize-space()='Save']")).click();

        // ❗ Duplicate => không ra danh sách, ở lại trang Create
        assertTrue(driver.getCurrentUrl().contains("/Departments/Create"),
                "Stay on Create page when duplicate code");

        // ❗ Lỗi server: Cannot create department.
        WebElement serverError = getServerErrorMessage();

        assertTrue(serverError.isDisplayed(), "Server error message displayed");
        assertEquals("Cannot create department.", serverError.getText().trim(),
                "Duplicate code server message matches expected");

        Teardown();
    }

    // ================= ASSERT FUNCTIONS =================

    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            System.out.println("[PASSED] " + message);
        } else {
            System.out.println("[FAILED] " + message);
        }
    }

    private static void assertEquals(String expected, String actual, String message) {
        if (expected.equals(actual)) {
            System.out.println("[PASSED] " + message);
        } else {
            System.out.println("[FAILED] " + message);
            System.out.println("   Expected: " + expected);
            System.out.println("   Actual  : " + actual);
        }
    }

    // Lỗi server thường hiển thị trong alert-danger hoặc text-danger
    private static WebElement getServerErrorMessage() {
        return driver.findElement(By.cssSelector(".alert-danger, .text-danger"));
    }

}

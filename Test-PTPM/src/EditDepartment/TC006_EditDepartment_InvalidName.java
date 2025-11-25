package EditDepartment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Initialization.Init;

public class TC006_EditDepartment_InvalidName extends Init {

    public static void main(String[] args) throws InterruptedException {

        SetUp("edge");
        driver.get("https://localhost:7222");

        System.out.println("=== TC006: Edit Department - Invalid Name ===");

        // Mở trang Departments
        driver.findElement(By.linkText("Departments")).click();
        goToDepartmentsList();

        // Tìm dòng chứa "Khoa Ngoại Tổng Hợp"
        WebElement row = driver.findElement(
                By.xpath("//table//tr[td[contains(text(),'Khoa Ngoại Tổng hợp')]]")
        );
        row.findElement(By.linkText("Edit")).click();
        driver.findElement(By.xpath("//input[@id='Code']")).sendKeys("D022");
        assertTrue(driver.getCurrentUrl().contains("/Departments/Edit"),
                "Navigated to Edit page");

        // Nhập tên không hợp lệ
        WebElement nameInput = driver.findElement(By.id("Name"));
        nameInput.clear();
        nameInput.sendKeys("@@@@");

        driver.findElement(By.xpath("//button[text()='Save']")).click();

        assertTrue(driver.getCurrentUrl().contains("/Departments/Edit"),
                "Stay on Edit page when invalid");

        // Lấy dòng lỗi
        WebElement nameError = getValidationMessage("Name");
        assertTrue(nameError.isDisplayed(), "Validation message displayed");
        assertEquals("Invalid department name", nameError.getText().trim());

        Teardown();
    }

    // ============================================================
    // HÀM HỖ TRỢ
    // ============================================================

    private static void goToDepartmentsList() {
        // Nếu hệ thống có trang listing thì không cần làm gì
        System.out.println("Navigated to Departments List.");
    }

    private static WebElement getValidationMessage(String fieldId) {
        return driver.findElement(
                By.xpath("//span[@data-valmsg-for='" + fieldId + "']")
        );
    }

    // In kết quả assert
    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            System.out.println("PASSED: " + message);
        } else {
            System.out.println("FAILED: " + message);
        }
    }

    private static void assertTrue(boolean condition) {
        if (condition) {
            System.out.println("PASSED");
        } else {
            System.out.println("FAILED");
        }
    }

    private static void assertEquals(String expected, String actual) {
        if (expected.equals(actual)) {
            System.out.println("✔ PASSED: Expected = Actual = " + actual);
        } else {
            System.out.println("✘ FAILED: Expected '" + expected + "' but got '" + actual + "'");
        }
    }
}

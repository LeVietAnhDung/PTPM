package EditDepartment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Initialization.Init;

public class TC007_EditDepartment_ChangeCodeToDuplicate extends Init {

    public static void main(String[] args) throws InterruptedException {

        SetUp("edge");
        driver.get("https://localhost:7222");

        System.out.println("=== TC007: Edit Department - Duplicate Code ===");

        driver.findElement(By.linkText("Departments")).click();
        goToDepartmentsList();

        // Tìm dòng chứa "Khoa Ngoại Tổng hợp"
        WebElement row = driver.findElement(
                By.xpath("//table//tr[td[contains(text(),'Khoa Ngoại Tổng hợp')]]")
        );
        row.findElement(By.linkText("Edit")).click();

        // Kiểm tra đúng trang Edit
        assertTrue(driver.getCurrentUrl().contains("/Departments/Edit"),
                "Navigated to Edit Department page");

        // Clear và nhập code đã tồn tại
        WebElement codeInput = driver.findElement(By.id("Code"));
        codeInput.clear();
        codeInput.sendKeys("D020");

        driver.findElement(By.xpath("//button[text()='Save']")).click();

        // Khi mã trùng => vẫn ở trang Edit
        assertTrue(driver.getCurrentUrl().contains("/Departments/Edit"),
                "Stay on Edit page after error");

        // Kiểm tra validation message
        WebElement codeError = getValidationMessage("Code");
        assertTrue(codeError.isDisplayed(), "Validation error visible");
        assertEquals("Code already exists", codeError.getText().trim());

        Teardown();
    }

    // ============================================
    //        HÀM HỖ TRỢ
    // ============================================

    private static void goToDepartmentsList() {
        System.out.println("Navigated to Departments List.");
    }

    private static WebElement getValidationMessage(String fieldId) {
        return driver.findElement(
            By.xpath("//span[@data-valmsg-for='" + fieldId + "']")
        );
    }

    private static void assertTrue(boolean condition, String msg) {
        if (condition) {
            System.out.println("PASSED: " + msg);
        } else {
            System.out.println("FAILED: " + msg);
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
            System.out.println("PASSED: Expected = Actual = " + actual);
        } else {
            System.out.println("FAILED: Expected '" + expected + "' but got '" + actual + "'");
        }
    }
}

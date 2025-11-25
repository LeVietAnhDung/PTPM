package DeleteDepartment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Initialization.Init;

public class TC008_DeleteDepartment_Valid extends Init {

    public static void main(String[] args) throws InterruptedException {

        SetUp("edge");
        driver.get("https://localhost:7222");

        System.out.println("=== TC008: Delete Department - Valid ===");

        driver.findElement(By.linkText("Departments")).click();
        goToDepartmentsList();

        // Tìm dòng chứa Khoa Nhi
        WebElement row = driver.findElement(
                By.xpath("//table//tr[td[contains(text(),'Khoa Nhi')]]")
        );

        row.findElement(By.linkText("Delete")).click();

        assertTrue(driver.getCurrentUrl().contains("/Departments/Delete"),
                "Navigated to Delete confirmation page");

        driver.findElement(By.xpath("//button[text()='Delete']")).click();

        assertTrue(driver.getCurrentUrl().contains("/Departments"),
                "Back to Departments list after delete");

        WebElement table = driver.findElement(By.tagName("table"));
        String tableText = table.getText();

        assertFalse(tableText.contains("Khoa Nhi"),
                "\"Khoa Nhi\" should be removed from the list");

        Teardown();
    }

    // ================================
    // HÀM HỖ TRỢ
    // ================================
    private static void goToDepartmentsList() {
        System.out.println("Navigated to Departments List.");
    }

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

    private static void assertFalse(boolean condition, String message) {
        if (!condition) {
            System.out.println("PASSED: " + message);
        } else {
            System.out.println("FAILED: " + message);
        }
    }

    private static void assertFalse(boolean condition) {
        if (!condition) {
            System.out.println("PASSED");
        } else {
            System.out.println("FAILED");
        }
    }
}

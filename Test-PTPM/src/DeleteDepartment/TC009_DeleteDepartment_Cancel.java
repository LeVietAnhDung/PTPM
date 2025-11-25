package DeleteDepartment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Initialization.Init;

public class TC009_DeleteDepartment_Cancel extends Init {

    public static void main(String[] args) throws InterruptedException {

        SetUp("edge");
        driver.get("https://localhost:7222");

        System.out.println("=== TC009: Delete Department - Cancel ===");

        driver.findElement(By.linkText("Departments")).click();
        goToDepartmentsList();

        // Tìm dòng chứa Khoa Nội
        WebElement row = driver.findElement(
                By.xpath("//table//tr[td[contains(text(),'Khoa Nội')]]")
        );

        row.findElement(By.linkText("Delete")).click();

        assertTrue(driver.getCurrentUrl().contains("/Departments/Delete"),
                "Navigated to Delete confirmation page");

        // Nhấn Cancel
        driver.findElement(By.xpath("//a[text()='Cancel']")).click();

        assertTrue(driver.getCurrentUrl().contains("/Departments"),
                "Returned to Departments list after Cancel");

        // Verify NOT deleted
        WebElement table = driver.findElement(By.tagName("table"));
        String tableText = table.getText();

        assertTrue(tableText.contains("Khoa Nội"),
                "\"Khoa Nội\" should still exist after Cancel");

        Teardown();
    }

    // ===============================
    // HÀM HỖ TRỢ
    // ===============================

    private static void goToDepartmentsList() {
        System.out.println("Navigated to Departments List.");
    }

    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            System.out.println("✔ PASSED: " + message);
        } else {
            System.out.println("✘ FAILED: " + message);
        }
    }

    private static void assertTrue(boolean condition) {
        if (condition) {
            System.out.println("✔ PASSED");
        } else {
            System.out.println("✘ FAILED");
        }
    }
}

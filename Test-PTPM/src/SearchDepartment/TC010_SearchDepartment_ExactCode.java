package SearchDepartment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Initialization.Init;

public class TC010_SearchDepartment_ExactCode extends Init {

    public static void main(String[] args) throws InterruptedException {

        SetUp("edge");
        driver.get("https://localhost:7222");

        System.out.println("=== TC010: Search Department by Exact Code ===");

        driver.findElement(By.linkText("Departments")).click();
        goToDepartmentsList();

        // Nhập mã D001
        WebElement searchBox = driver.findElement(
                By.cssSelector("input[placeholder='Search by code or name']"));
        searchBox.clear();
        searchBox.sendKeys("Khoa Tim");

        driver.findElement(By.xpath("//button[text()='Search']")).click();

        // Kiểm tra kết quả
        WebElement table = driver.findElement(By.tagName("table"));
        String tableText = table.getText();

        assertTrue(tableText.contains("D001"),
                "Search result contains department code D001");
        assertTrue(tableText.contains("Khoa Tim"),
                "Search result contains department name Khoa Tim");

        // Kiểm tra chỉ có 1 dòng kết quả
        int rowCount = driver.findElements(By.xpath("//table/tbody/tr")).size();
        assertEquals(1, rowCount,
                "Only one search result row should be displayed");

        Teardown();
    }

    // ======================
    // HÀM HỖ TRỢ
    // ======================

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

    private static void assertEquals(int expected, int actual, String message) {
        if (expected == actual) {
            System.out.println("PASSED: " + message);
        } else {
            System.out.println("FAILED: " + message +
                    " (Expected: " + expected + ", Actual: " + actual + ")");
        }
    }
}

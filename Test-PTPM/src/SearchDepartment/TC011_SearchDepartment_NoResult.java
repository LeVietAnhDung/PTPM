package SearchDepartment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Initialization.Init;

public class TC011_SearchDepartment_NoResult extends Init {

    public static void main(String[] args) throws InterruptedException {

        SetUp("edge");
        driver.get("https://localhost:7222");

        System.out.println("=== TC011: Search Department - No Result ===");

        driver.findElement(By.linkText("Departments")).click();
        goToDepartmentsList();

        // Nhập giá trị không tồn tại
        WebElement searchBox = driver.findElement(
                By.cssSelector("input[placeholder='Search by code or name']"));

        searchBox.clear();
        searchBox.sendKeys("ABCXYZ");

        driver.findElement(By.xpath("//button[text()='Search']")).click();

        // Lấy số dòng hiển thị sau tìm kiếm
        int rowCount = driver.findElements(By.xpath("//table/tbody/tr")).size();

        boolean noRow = rowCount == 0;
        boolean hasNoResultText = driver.getPageSource().contains("No departments found");

        assertTrue(noRow || hasNoResultText,
                "No results displayed when searching for non-existing department");

        Teardown();
    }

    // ======================
    // HÀM HỖ TRỢ
    // ======================

    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            System.out.println("PASSED: " + message);
        } else {
            System.out.println("FAILED: " + message);
        }
    }

    private static void goToDepartmentsList() {
        System.out.println("Navigated to Departments List.");
    }
}

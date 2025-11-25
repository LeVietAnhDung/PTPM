package SearchDepartment;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import Initialization.Init;

public class TC012_SearchDepartment_EmptyText extends Init {

    public static void main(String[] args) throws InterruptedException {

        SetUp("edge");
        driver.get("https://localhost:7222");

        System.out.println("=== TC012: Search Department - Empty Search Text ===");

        driver.findElement(By.linkText("Departments")).click();
        goToDepartmentsList();

        // Clear search box (để trống)
        WebElement searchBox = driver.findElement(
                By.cssSelector("input[placeholder='Search by code or name']"));
        searchBox.clear();

        // Nhấn nút Search
        driver.findElement(By.xpath("//button[text()='Search']")).click();

        Thread.sleep(700); // cho trang hiển thị thông báo

        String expectedMsg = "Hãy điền mã hoặc tên";
        String actualMsg = "";

        try {
            // Tìm element hiển thị message (phải có id='msg' hoặc class='text-danger')
            WebElement msgElement = driver.findElement(By.id("msg"));
            actualMsg = msgElement.getText().trim();
        } catch (NoSuchElementException e) {
            throw new AssertionError("FAILED: Không tìm thấy thông báo trên giao diện!");
        }

        if (!actualMsg.equals(expectedMsg)) {
            throw new AssertionError(
                "FAILED: Expected message '" + expectedMsg + "' but got '" + actualMsg + "'"
            );
        }

        System.out.println("✔ Test PASSED: Web hiển thị đúng thông báo");

        Teardown();
    }

    private static void goToDepartmentsList() {
        try { Thread.sleep(800); } catch (Exception e) {}
    }
}

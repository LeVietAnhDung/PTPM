package CreateDepartment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Initialization.Init;

public class TC003_CreateDepartment_InvalidNameNumeric extends Init {

    public static void main(String[] args) throws InterruptedException {

        SetUp("edge");
        driver.get("https://localhost:7222");

        driver.findElement(By.linkText("Departments")).click();
        driver.findElement(By.linkText("+ New Department")).click();

        driver.findElement(By.id("Code")).sendKeys("D002");
        driver.findElement(By.id("Name")).sendKeys("1234"); // invalid numeric name

        WebElement activeCheckbox = driver.findElement(By.id("IsActive"));
        if (!activeCheckbox.isSelected()) {
            activeCheckbox.click();
        }

        WebElement saveBtn = driver.findElement(By.xpath("//button[normalize-space()='Save']"));
        saveBtn.click();

        // Stay on create page when validation fails
        assertTrue(driver.getCurrentUrl().contains("/Departments/Create"),
                "Stay on Create Department page");

        // Get error message for Name field
        WebElement nameError = getValidationMessage("Name");

        assertTrue(nameError.isDisplayed(), "Name validation error displayed");

        assertEquals("Invalid department name", nameError.getText().trim(),
                "Name validation message matches expected");

        Teardown();
    }

    // ------------------- CUSTOM ASSERT FUNCTIONS -------------------

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

    // Lấy lỗi validation theo fieldName (Razor MVC)
    private static WebElement getValidationMessage(String fieldId) {
        return driver.findElement(By.cssSelector("[data-valmsg-for='" + fieldId + "']"));
    }

}

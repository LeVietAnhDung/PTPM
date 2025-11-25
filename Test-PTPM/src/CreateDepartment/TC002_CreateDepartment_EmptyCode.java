package CreateDepartment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Initialization.Init;

public class TC002_CreateDepartment_EmptyCode extends Init {
	public static void main(String[] args) throws InterruptedException {

		SetUp("edge");
		driver.get("https://localhost:7222");

		driver.findElement(By.linkText("Departments")).click();
		driver.findElement(By.linkText("+ New Department")).click();

		driver.findElement(By.id("Code")).sendKeys(""); // empty code
		driver.findElement(By.id("Name")).sendKeys("Khoa Tim");

		WebElement activeCheckbox = driver.findElement(By.id("IsActive"));
		if (!activeCheckbox.isSelected()) {
			activeCheckbox.click();
		}

		WebElement saveBtn = driver.findElement(By.xpath("//button[normalize-space()='Save']"));
		saveBtn.click();

		assertTrue(driver.getCurrentUrl().contains("Departments/Create"), 
				"Stay on Create Department page when validation fails");

		// Lấy lỗi Code
		WebElement codeError = getValidationMessage("Code");

		assertTrue(codeError.isDisplayed(), "Code error message displayed");

		assertEquals("The Code field is required.", codeError.getText().trim(),
				"Code validation message matches expected");
		
		Teardown();
	}

	// ---------------- CUSTOM ASSERT FUNCTIONS ----------------
	
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

	// Lấy validation message theo field
	private static WebElement getValidationMessage(String fieldId) {
		// Lỗi luôn nằm trong span hoặc div có data-valmsg-for
		return driver.findElement(By.cssSelector("[data-valmsg-for='" + fieldId + "']"));
	}

}

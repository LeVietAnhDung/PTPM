package CreateDepartment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Initialization.Init;

public class TC001_CreateDepartment_ValidInputs extends Init {
	public static void main(String[] args) throws InterruptedException {
	
		SetUp("edge");
		driver.get("https://localhost:7222");
		
		driver.findElement(By.linkText("Departments")).click();
		driver.findElement(By.linkText("+ New Department")).click();
		driver.findElement(By.id("Code")).sendKeys("D011");
		driver.findElement(By.id("Name")).sendKeys("Khoa Tim");
		
		WebElement activeCheckbox = driver.findElement(By.id("IsActive"));
        if (!activeCheckbox.isSelected()) {
            activeCheckbox.click();
        }

        WebElement saveBtn = driver.findElement(By.xpath("//button[normalize-space()='Save']"));
        saveBtn.click();

        assertTrue(driver.getCurrentUrl().contains("Departments"), "Redirect to Department List");

        WebElement table = driver.findElement(By.tagName("table"));
        String tableText = table.getText();
      //  assertTrue(tableText.contains("D011"), "Department code displayed");
        assertTrue(tableText.contains("Khoa Tim"), "Department name displayed");
        
        Teardown();
    }
		
	// ----- HÀM ASSERT HIỆN KẾT QUẢ LÊN MÀN HÌNH -----
	private static void assertTrue(boolean condition, String message) {
	    if (condition) {
	        System.out.println("[PASSED] " + message);
	    } else {
	        System.out.println("[FAILED] " + message);
	    }
	}

}

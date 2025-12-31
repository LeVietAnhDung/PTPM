package com.hospitaltriage.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class Navbar {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public Navbar(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private WebElement find(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public boolean isLinkVisible(String linkText) {
        return driver.findElements(By.linkText(linkText)).stream().anyMatch(WebElement::isDisplayed);
    }

    public void clickLink(String linkText) {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText))).click();
    }

    public void openCatalogDropdown() {
        // "Danh mục" dropdown toggle
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Danh mục"))).click();
    }

    public void goToDepartments() {
        openCatalogDropdown();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Khoa khám"))).click();
    }

    public void goToDoctors() {
        openCatalogDropdown();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Bác sĩ"))).click();
    }

    public void logout() {
        // Logout is rendered as <button>Logout</button> inside a form
        var locator = By.xpath("//button[normalize-space()='Logout']");
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        // ensure we see Login button afterwards
        wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Login")));
    }

    public void loginButtonClick() {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Login"))).click();
    }
}

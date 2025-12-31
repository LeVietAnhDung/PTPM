package com.hospitaltriage.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        // tăng timeout để giảm flaky (bạn muốn 10s thì đổi lại 10)
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    protected WebElement $(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        click(el);
    }

    /**
     * Robust click for elements that may be covered by sticky navbars/toasts.
     */
    protected void click(WebElement el) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            // Scroll into view then try again; fall back to JS click.
            try {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block:'center',inline:'center'});",
                        el
                );
                wait.until(ExpectedConditions.elementToBeClickable(el)).click();
            } catch (Exception ignored) {
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                } catch (Exception ignored2) {
                    throw e;
                }
            }
        }
    }

    protected void type(By locator, String text) {
        WebElement el = $(locator);
        el.clear();
        if (text != null && !text.isEmpty()) {
            el.sendKeys(text);
        }
    }

    /**
     * Force-set the value of an <input>/<select> using JavaScript.
     * Useful for negative testing (e.g., set an invalid option value).
     */
    protected void setValueByJs(By locator, String value) {
        WebElement el = $(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                        "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
                el,
                value
        );
    }

    /**
     * Clear an input in a more robust way (especially for type='date').
     */
    protected void clearInput(By locator) {
        WebElement el = $(locator);
        try {
            el.clear();
        } catch (Exception ignored) {
        }
        try {
            el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            el.sendKeys(Keys.DELETE);
        } catch (Exception ignored) {
        }
        // last resort
        try {
            setValueByJs(locator, "");
        } catch (Exception ignored) {
        }
    }

    protected void select(By locator, String visibleText) {
        WebElement el = $(locator);
        var sel = new org.openqa.selenium.support.ui.Select(el);

        // Common locale mapping: VN labels in tests, EN enum labels in the app.
        String mapped = mapCommonVisibleText(visibleText);

        try {
            sel.selectByVisibleText(mapped);
            return;
        } catch (NoSuchElementException ignored) {
            // Fall through
        }

        // Try case-insensitive match against actual options.
        for (var opt : sel.getOptions()) {
            String t = opt.getText() == null ? "" : opt.getText().trim();
            if (!t.isBlank() && t.equalsIgnoreCase(mapped.trim())) {
                opt.click();
                return;
            }
        }

        // Final fallback: allow selecting by well-known enum values.
        // (e.g., Gender: Male=1, Female=2, Other=3)
        if ("Male".equalsIgnoreCase(mapped) || "Nam".equalsIgnoreCase(visibleText)) {
            try { sel.selectByValue("1"); return; } catch (Exception ignored) {}
        }
        if ("Female".equalsIgnoreCase(mapped) || "Nữ".equalsIgnoreCase(visibleText) || "Nu".equalsIgnoreCase(visibleText)) {
            try { sel.selectByValue("2"); return; } catch (Exception ignored) {}
        }
        if ("Other".equalsIgnoreCase(mapped) || "Khác".equalsIgnoreCase(visibleText) || "Khac".equalsIgnoreCase(visibleText)) {
            try { sel.selectByValue("3"); return; } catch (Exception ignored) {}
        }

        // If we still cannot select, rethrow with a clearer error.
        throw new NoSuchElementException("Cannot locate option with text: " + visibleText + " (mapped=" + mapped + ")");
    }

    private static String mapCommonVisibleText(String visibleText) {
        if (visibleText == null) return "";
        String v = visibleText.trim();
        // Vietnamese -> English enum labels used by Html.GetEnumSelectList<Gender>()
        if (v.equalsIgnoreCase("Nam")) return "Male";
        if (v.equalsIgnoreCase("Nữ") || v.equalsIgnoreCase("Nu")) return "Female";
        if (v.equalsIgnoreCase("Khác") || v.equalsIgnoreCase("Khac")) return "Other";
        if (v.equalsIgnoreCase("Không rõ") || v.equalsIgnoreCase("Khong ro")) return "Unknown";
        return v;
    }

    /**
     * Collect all error texts shown on the current page.
     * Works with TempData alerts and MVC validation summaries/spans.
     */
    public String collectErrorTexts() {
        return driver.findElements(By.cssSelector(".alert-danger, .validation-summary-errors, .text-danger"))
                .stream()
                .map(WebElement::getText)
                .filter(t -> t != null && !t.isBlank())
                .distinct()
                .reduce((a, b) -> a + " | " + b)
                .orElse("");
    }

    protected void selectByValue(By locator, String value) {
        WebElement el = $(locator);
        new org.openqa.selenium.support.ui.Select(el).selectByValue(value);
    }

    protected boolean isPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String title() {
        return driver.getTitle();
    }

    public String url() {
        return driver.getCurrentUrl();
    }

    public void acceptConfirmIfPresent() {
        try {
            Alert a = driver.switchTo().alert();
            a.accept();
        } catch (NoAlertPresentException ignored) {
        }
    }

    public String pageSource() {
        return driver.getPageSource();
    }

    public String pageText() {
        return driver.findElement(By.tagName("body")).getText();
    }

    public boolean bodyContainsAny(String... keywords) {
        String t = "";
        try {
            t = pageText().toLowerCase();
        } catch (Exception ignored) {}
        for (String k : keywords) {
            if (k != null && !k.isBlank() && t.contains(k.toLowerCase())) return true;
        }
        return false;
    }

    /**
     * Negative tests: chỉ cần có dấu hiệu lỗi là PASS (không bắt đúng câu lỗi).
     */
    public boolean hasAnyError() {
        // 1) Selector lỗi phổ biến (Bootstrap/MVC/Toast/SweetAlert + aria-invalid)
        boolean byElements = driver.findElements(By.cssSelector(
                ".alert-danger, .alert-warning," +
                ".alert.alert-danger, .alert.alert-warning," +
                ".validation-summary-errors, .field-validation-error," +
                ".text-danger," +
                ".toast-error, .toast-warning," +
                ".swal2-icon-error, .swal2-error," +
                "input.input-validation-error, select.input-validation-error, textarea.input-validation-error," +
                "[aria-invalid='true']"
        )).size() > 0;

        if (byElements) return true;

        // 2) Fallback theo từ khóa (VN + EN) → không cần đúng câu
        String t = "";
        try {
            t = pageText().toLowerCase();
        } catch (Exception ignored) {}

        return t.contains("lỗi")
                || t.contains("không hợp lệ")
                || t.contains("bắt buộc")
                || t.contains("không được")
                || t.contains("thất bại")
                || t.contains("error")
                || t.contains("invalid")
                || t.contains("required");
    }

    /**
     * Chờ cho lỗi xuất hiện (giảm flaky do UI render chậm).
     */
    public void waitForAnyError() {
        wait.until(d -> hasAnyError());
    }
}

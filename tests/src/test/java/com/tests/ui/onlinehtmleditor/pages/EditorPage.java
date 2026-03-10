package com.tests.ui.onlinehtmleditor.pages;

import com.framework.core.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

//CKEditor 5 based editor 
public class EditorPage extends BasePage {

    private static final String URL = "https://onlinehtmleditor.dev";

    // Bold & Underline button locators are looking for partial matches because of
    // OS specific locator handling (e.g. "Bold (⌘B)" on Mac vs "Bold (Ctrl+B)" on
    // Windows)
    private static final By BOLD_BUTTON = By.cssSelector("button[data-cke-tooltip-text*='Bold']");
    private static final By UNDERLINE_BUTTON = By.cssSelector("button[data-cke-tooltip-text*='Underline']");
    private static final By EDITOR_BODY = By.cssSelector("div.ck-editor__editable[contenteditable='true']");

    public EditorPage(WebDriver driver) {
        super(driver);
    }

    public EditorPage open() {
        driver.get(URL);
        focusEditor();
        return this;
    }

    public boolean isLoaded() {
        boolean correctUrl = driver.getCurrentUrl().contains("onlinehtmleditor.dev");
        boolean editorVisible = wait.on(EDITOR_BODY).isVisible();
        boolean toolbarVisible = wait.on(BOLD_BUTTON).isVisible();
        return correctUrl && editorVisible && toolbarVisible;
    }

    private void assertButtonState(By button, String name, boolean expectedActive) {
        String expected = String.valueOf(expectedActive);
        log.debug("Waiting for {} button aria-pressed='{}'", name, expected);
        wait.on(button).hasAttribute("aria-pressed", expected);
    }

    public EditorPage typeBold(String text) {
        log.debug("Typing bold text: '{}'", text);
        click(BOLD_BUTTON);
        assertButtonState(BOLD_BUTTON, "Bold", true);
        typeInEditor(text);
        click(BOLD_BUTTON);
        assertButtonState(BOLD_BUTTON, "Bold", false);
        return this;
    }

    public EditorPage typeUnderline(String text) {
        log.debug("Typing underlined text: '{}'", text);
        click(UNDERLINE_BUTTON);
        assertButtonState(UNDERLINE_BUTTON, "Underline", true);
        typeInEditor(text);
        click(UNDERLINE_BUTTON);
        assertButtonState(UNDERLINE_BUTTON, "Underline", false);
        return this;
    }

    public EditorPage typeText(String text) {
        log.debug("Typing plain text: '{}'", text);
        typeInEditor(text);
        return this;
    }

    // Gets the inner HTML of the editor body to verify that formatting tags are
    // correctly applied
    public String getEditorInnerHtml() {
        String html = (String) ((JavascriptExecutor) driver).executeScript(
                "return document.querySelector(\"div.ck-editor__editable[contenteditable='true']\").innerHTML;");
        log.debug("Editor inner HTML: {}", html);
        return html;
    }

    private void focusEditor() {
        new Actions(driver).click(wait.on(EDITOR_BODY).visible()).perform();
    }

    private void typeInEditor(String text) {
        new Actions(driver).sendKeys(text).perform();
    }
}

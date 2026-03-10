package com.tests.ui.saucedemo;

import com.framework.utils.CredentialReader;
import com.tests.ui.saucedemo.pages.InventoryPage;
import com.tests.ui.saucedemo.pages.LoginPage;
import org.openqa.selenium.WebDriver;

class LoginHelper {

    private final WebDriver driver;

    LoginHelper(WebDriver driver) {
        this.driver = driver;
    }

    InventoryPage loginAs(String userKey) {
        CredentialReader.Credentials credentials = CredentialReader.loadUser(userKey);
        return new LoginPage(driver)
                .open()
                .loginAs(credentials.username(), credentials.password());
    }
}

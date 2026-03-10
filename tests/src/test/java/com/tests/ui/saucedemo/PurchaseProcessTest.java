package com.tests.ui.saucedemo;

import com.framework.core.BaseTest;
import com.framework.utils.CredentialReader;
import com.framework.utils.CredentialReader.Credentials;
import com.framework.utils.CredentialReader.CheckoutInfo;
import com.tests.ui.saucedemo.pages.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Case 1 – Purchase Process")
class PurchaseProcessTest extends BaseTest {

        private static Credentials credentials;
        private static CheckoutInfo checkoutInfo;
        private static List<String> itemsToBuy;

        @BeforeAll
        static void loadTestData() {
                credentials = CredentialReader.load("credentials.json");
                checkoutInfo = CredentialReader.loadCheckoutInfo("testdata.json");
                itemsToBuy = CredentialReader.loadItems("testdata.json");
        }

        @Test
        @DisplayName("User can log in, add items to cart, and complete checkout")
        void completePurchaseProcess() {

                // Step 1 — Login
                log.info("Step 1: Opening login page and signing in as '{}'", credentials.username());
                InventoryPage inventoryPage = new LoginPage(driver)
                                .open()
                                .loginAs(credentials.username(), credentials.password());

                assertTrue(inventoryPage.isLoaded(), "Inventory page should be loaded after login");

                // Step 2 — Add items to cart
                log.info("Step 2: Adding items to cart: {}", itemsToBuy);
                itemsToBuy.forEach(inventoryPage::addToCart);

                // Step 3 — Validate cart badge count
                int cartCount = inventoryPage.getCartCount();
                log.info("Step 3: Cart badge shows {} item(s)", cartCount);
                assertEquals(itemsToBuy.size(), cartCount,
                                "Cart badge should show " + itemsToBuy.size() + " items");

                // Step 4 — Go to cart and verify contents
                log.info("Step 4: Navigating to cart");
                CartPage cartPage = inventoryPage.goToCart();
                assertTrue(cartPage.getCartItemNames().containsAll(itemsToBuy),
                                "Cart should contain all selected items");

                // Step 5 — Checkout info and order summary
                log.info("Step 5: Filling checkout info");
                CheckoutStepTwoPage summaryPage = cartPage
                                .proceedToCheckout()
                                .fillDetailsAndContinue(
                                                checkoutInfo.firstName(),
                                                checkoutInfo.lastName(),
                                                checkoutInfo.postalCode());

                assertTrue(summaryPage.getOrderItemNames().containsAll(itemsToBuy),
                                "Order summary should contain all purchased items");

                // Step 6 — Finish order and validate confirmation
                log.info("Step 6: Finishing order and validating confirmation");
                CheckoutCompletePage completePage = summaryPage.finishOrder();

                assertEquals("Thank you for your order!", completePage.getConfirmationHeader(),
                                "Confirmation header text should match");
        }
}

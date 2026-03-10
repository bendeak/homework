package com.tests.ui.onlinehtmleditor;

import com.framework.core.BaseTest;
import com.tests.ui.onlinehtmleditor.pages.EditorPage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Case 3 – Rich Text Editor")
class RichTextEditorTest extends BaseTest {

        private static final String BOLD_TEXT = "Automation";
        private static final String UNDERLINE_TEXT = "Test";
        private static final String PLAIN_TEXT = " Example";

        @Test
        @DisplayName("Bold, underline and plain text are correctly encoded in the editor")
        void richTextFormattingIsAppliedCorrectly() {

                // Step 1 — Open the editor
                log.info("Step 1: Opening rich text editor");
                EditorPage editorPage = new EditorPage(driver).open();
                assertTrue(editorPage.isLoaded(), "Rich text editor page should be loaded");

                // Step 2 & 3 — Type "Automation" in bold, then an empty space
                log.info("Step 2: Typing '{}' in bold", BOLD_TEXT);
                editorPage.typeBold(BOLD_TEXT);

                editorPage.typeText(" ");

                // Step 4 — Type "Test" with underline, then an empty space
                log.info("Step 3: Typing '{}' with underline", UNDERLINE_TEXT);
                editorPage.typeUnderline(UNDERLINE_TEXT);

                // Type " Example" as plain text
                log.info("Step 4: Typing '{}' as plain text", PLAIN_TEXT);
                editorPage.typeText(PLAIN_TEXT);

                // Step 5 — Validate formatting in the editor's inner HTML
                log.info("Step 5: Validating editor content and formatting");
                String html = editorPage.getEditorInnerHtml();
                log.info("Editor HTML: {}", html);

                assertAll("Editor content and formatting",
                                () -> assertTrue(html.contains("<strong>" + BOLD_TEXT + "</strong>"),
                                                "'" + BOLD_TEXT + "' should be wrapped in <strong>"),
                                () -> assertTrue(html.contains("<u>" + UNDERLINE_TEXT + "</u>"),
                                                "'" + UNDERLINE_TEXT + "' should be wrapped in <u>"),
                                () -> assertTrue(html.contains(PLAIN_TEXT.trim()),
                                                "'" + PLAIN_TEXT.trim() + "' should appear as plain text"));
        }
}

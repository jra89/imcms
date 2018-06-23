package com.imcode.imcms.domain.component;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PropertyBasedTextContentFilterTest {

    private final String[] allowedTags = {
            "div",
            "span",
            "p",
            "b",
            "h1"
    };
    private final String[] badTagsArr = {
            "iframe",
            "script"
    };

    @InjectMocks
    private PropertyBasedTextContentFilter textContentFilter;

    @Before
    public void setUp() throws Exception {
        textContentFilter.addHtmlTagsToWhiteList(allowedTags);
    }

    @Test
    public void testBadTags() {
        final String textWithBadTags = "<" + badTagsArr[0] + " class=\"some-class\">alalal</" + badTagsArr[0] + ">"
                + "test text"
                + "<" + badTagsArr[1] + " src=\"http://blabla.com\"></" + badTagsArr[1] + ">"
                + "<" + badTagsArr[1] + ">alert('!!!')</" + badTagsArr[1] + ">"
                + "test text";

        final String expectedCleanedText = "alalal"
                + "test text"
                + "test text";

        final String textAfterCleanup = textContentFilter.cleanText(textWithBadTags);
        assertEquals(expectedCleanedText, textAfterCleanup);
    }

    @Test
    public void testBadScriptTag() {
        final String textWithBadTag = "<script>this is a not allowed tag test</script>";
        final String expectedCleanedText = "";
        final String textAfterCleanup = textContentFilter.cleanText(textWithBadTag);
        assertEquals(expectedCleanedText, textAfterCleanup);
    }

    @Test
    public void testBadTag() {
        final String badTag = "test-tag";
        final String textWithBadTag = "<" + badTag + ">this is a not allowed tag test</" + badTag + ">";
        final String expectedCleanedText = "this is a not allowed tag test";
        final String textAfterCleanup = textContentFilter.cleanText(textWithBadTag);
        assertEquals(expectedCleanedText, textAfterCleanup);
    }

    @Test
    public void testAllowedTag() {
        final String textWithAllowedTag = "<div> this is allowed tag test </div>";
        final String textAfterCleanup = textContentFilter.cleanText(textWithAllowedTag);
        assertEquals(textWithAllowedTag, textAfterCleanup);
    }

    @Test
    public void testTextWithSpecialSymbolButNotTag() {
        final String textWithSpecialSymbolButNotTag = "text with tags symbol \"<\" but this is not a tag";
        final String textAfterCleanup = textContentFilter.cleanText(textWithSpecialSymbolButNotTag);
        assertEquals(textWithSpecialSymbolButNotTag, textAfterCleanup);
    }

    @Test
    public void testTextWithoutTags() {
        final String textWithoutTags = "text without tags";
        final String textAfterCleanup = textContentFilter.cleanText(textWithoutTags);
        assertEquals(textWithoutTags, textAfterCleanup);
    }

    @Test
    public void testEmptyText() {
        final String emptyText = "";
        final String textAfterCleanup = textContentFilter.cleanText(emptyText);
        assertEquals(emptyText, textAfterCleanup);
    }

    @Test
    public void cleanText_When_OkTextWithIllegalTextTogether_Expected_IllegalTextRemoved() {
        final String correctFirstPart = "some other <b>text</b> here";
        final String wrongPart = "<script>console.log('test');console.log('pshhh');</script>";
        final String correctTail = "and <h1>here</h1>";
        final String expected = correctFirstPart + correctTail;

        final String cleaned = textContentFilter.cleanText(correctFirstPart + wrongPart + correctTail);

        assertEquals(expected, cleaned);
    }
}
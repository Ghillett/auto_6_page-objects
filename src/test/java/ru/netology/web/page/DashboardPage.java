package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.conditions.CssValue;
import lombok.val;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

import ru.netology.web.page.TransferPage;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public String firstCardId = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    public String secondCardId = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(String id) {
        ElementsCollection newCards = cards.filter(attribute("data-test-id", id));
        return extractBalance(newCards.first().text());
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransferPage transferToFirst() {
        $x("//div[@data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']//button").click();
        return new TransferPage();
    }

    public TransferPage transferToSecond() {
        $x("//div[@data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']//button").click();
        return new TransferPage();
    }
}
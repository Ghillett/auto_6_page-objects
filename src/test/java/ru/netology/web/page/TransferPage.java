package ru.netology.web.page;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$x;

import ru.netology.web.data.DataHelper;

public class TransferPage {

    public DashboardPage transfer(String amount, DataHelper.CardInfo cardInfo) {
        $x("//*[@data-test-id = 'amount']//input").setValue(amount);
        $x("//*[@data-test-id = 'from']//input").setValue(cardInfo.getCardNumber());
        $x("//*[@data-test-id = 'action-transfer']").click();
        return new DashboardPage();
    }

    public void transferWithError(String amount, DataHelper.CardInfo cardInfo) {
        $x("//*[@data-test-id = 'amount']//input").setValue(amount);
        $x("//*[@data-test-id = 'from']//input").setValue(cardInfo.getCardNumber());
        $x("//*[@data-test-id = 'action-transfer']").click();
        $x("//*[@data-test-id = 'error-notification']/div[@class = 'notification__content']").shouldBe(Condition.visible);
    }
}
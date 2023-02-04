package ru.netology.web.page;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$x;

public class TransferPage {

    String firstCard = "5559 0000 0000 0001";
    String secondCard = "5559 0000 0000 0002";
    String thirdCard = "5559 0000 0000 0003";

    public DashboardPage toFirst(String amount) {
        $x("//*[@data-test-id = 'amount']//input").setValue(amount);
        $x("//*[@data-test-id = 'from']//input").setValue(secondCard);
        $x("//*[@data-test-id = 'action-transfer']").click();
        return new DashboardPage();
    }

    public DashboardPage toSecond(String amount) {
        $x("//*[@data-test-id = 'amount']//input").setValue(amount);
        $x("//*[@data-test-id = 'from']//input").setValue(firstCard);
        $x("//*[@data-test-id = 'action-transfer']").click();
        return new DashboardPage();
    }

    public DashboardPage toThird(String amount) {
        $x("//*[@data-test-id = 'amount']//input").setValue(amount);
        $x("//*[@data-test-id = 'from']//input").setValue(thirdCard);
        $x("//*[@data-test-id = 'action-transfer']").click();
        return new DashboardPage();
    }

    public void getError() {
        $x("//*[@data-test-id = 'error-notification']/div[@class = 'notification__content']").shouldBe(Condition.visible);
    }
}
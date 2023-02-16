package ru.netology.web.test;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class MoneyTransferTest {

    public final int startMoney = 10000;

    @BeforeEach
    public void openSite() {
        open("http://localhost:9999");
    }

    @AfterEach
    public void toDefault() {
        open("http://localhost:9999");

        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));
        var firstCardId = DataHelper.getFirstCardInfo().getTestId();
        var secondCardId = DataHelper.getSecondCardInfo().getTestId();

        int firstCardBalance = dashboardPage.getCardBalance(firstCardId);
        int secondCardBalance = dashboardPage.getCardBalance(secondCardId);

        if (firstCardBalance > startMoney) {
            int difference = firstCardBalance - startMoney;
            var transferPage = dashboardPage.transferToSecond();
            transferPage.transfer(String.valueOf(difference), DataHelper.getFirstCardInfo());
        }
        if (secondCardBalance > startMoney) {
            int difference = secondCardBalance - startMoney;
            var transferPage = dashboardPage.transferToFirst();
            transferPage.transfer(String.valueOf(difference), DataHelper.getSecondCardInfo());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1, 10001, 9999",
            "2, 10002, 9998"
    })
    public void shouldTestTransferToFirstCard(String amount, int expectedBalanceFirstCard, int expectedBalanceSecondCard) {

        var loginPage = new LoginPage();
        var firstCardId = DataHelper.getFirstCardInfo().getTestId();
        var secondCardId = DataHelper.getSecondCardInfo().getTestId();

        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));
        var transferPage = dashboardPage.transferToFirst();
        transferPage.transfer(amount, DataHelper.getSecondCardInfo());

        int actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        int actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);

        Assertions.assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        Assertions.assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 10001, 9999",
            "2, 10002, 9998"
    })
    public void shouldTestTransferToSecondCard(String amount, int expectedBalanceSecondCard, int expectedBalanceFirstCard) {

        var loginPage = new LoginPage();
        var firstCardId = DataHelper.getFirstCardInfo().getTestId();
        var secondCardId = DataHelper.getSecondCardInfo().getTestId();

        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));
        var transferPage = dashboardPage.transferToSecond();
        transferPage.transfer(amount, DataHelper.getFirstCardInfo());

        int actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        int actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);

        Assertions.assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
        Assertions.assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
    }

    @Test
    public void shouldTestTransferToFirstCardOverLimit() {

        String amount = "10001";
        int expectedBalanceFirstCard = 20001;
        int expectedBalanceSecondCard = -1;

        var loginPage = new LoginPage();
        var firstCardId = DataHelper.getFirstCardInfo().getTestId();
        var secondCardId = DataHelper.getSecondCardInfo().getTestId();

        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));
        var transferPage = dashboardPage.transferToFirst();
        transferPage.transfer(amount, DataHelper.getSecondCardInfo());

        int actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardId);
        int actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardId);

        Assertions.assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        Assertions.assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    public void shouldTestTransferToSecondCardOverLimit() {

        String amount = "10001";
        int expectedBalanceSecondCard = 20001;
        int expectedBalanceFirstCard = -1;

        var loginPage = new LoginPage();
        var firstCardId = DataHelper.getFirstCardInfo().getTestId();
        var secondCardId = DataHelper.getSecondCardInfo().getTestId();

        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));
        var transferPage = dashboardPage.transferToSecond();
        transferPage.transfer(amount, DataHelper.getFirstCardInfo());

        transferPage.getError();
    }

    @Test
    public void shouldTestTransferFromWrongCard() {

        String amount = "1";

        var loginPage = new LoginPage();

        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));
        var transferPage = dashboardPage.transferToFirst();
        transferPage.transfer(amount, DataHelper.getWrongCardInfo());
        transferPage.getError();
    }
}
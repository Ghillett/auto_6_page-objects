package ru.netology.web.test;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;
import ru.netology.web.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class MoneyTransferTest {

    public final int startMoney = 10000;

    public String firstCardId = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    public String secondCardId = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";

    @BeforeEach
    public void openSite() {
        open("http://localhost:9999");
    }

    @AfterEach
    public void toDefault() {
        open("http://localhost:9999");

        var loginPage = new LoginPage();
        var transferPage = new TransferPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));

        int firstCardBalance = dashboardPage.getCardBalance(firstCardId);
        int secondCardBalance = dashboardPage.getCardBalance(secondCardId);

        if (firstCardBalance > startMoney) {
            int difference = firstCardBalance - startMoney;
            dashboardPage.transferToSecond();
            transferPage.toSecond(String.valueOf(difference));
        }
        if (secondCardBalance > startMoney) {
            int difference = secondCardBalance - startMoney;
            dashboardPage.transferToFirst();
            transferPage.toFirst(String.valueOf(difference));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1, 10001",
            "2, 10002"
    })
    public void shouldTestTransferToFirstCard(String amount, int expected) {

        var loginPage = new LoginPage();
        var transferPage = new TransferPage();

        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));
        dashboardPage.transferToFirst();
        transferPage.toFirst(amount);
        int actual = dashboardPage.getCardBalance(firstCardId);

        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 10001",
            "2, 10002"
    })
    public void shouldTestTransferToSecondCard(String amount, int expected) {

        var loginPage = new LoginPage();
        var transferPage = new TransferPage();

        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));
        dashboardPage.transferToSecond();
        transferPage.toSecond(amount);
        int actual = dashboardPage.getCardBalance(secondCardId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldTestTransferToFirstCardOverLimit() {

        String amount = "10001";
        int expected = 20001;

        var loginPage = new LoginPage();
        var transferPage = new TransferPage();

        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));
        dashboardPage.transferToFirst();
        transferPage.toFirst(amount);
        int actual = dashboardPage.getCardBalance(firstCardId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldTestTransferToSecondCardOverLimit() {

        String amount = "10001";
        int expected = 20001;
        var loginPage = new LoginPage();
        var transferPage = new TransferPage();

        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));
        dashboardPage.transferToSecond();
        transferPage.toSecond(amount);
        int actual = dashboardPage.getCardBalance(secondCardId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldTestTransferToWrongCard() {

        String amount = "1";

        var loginPage = new LoginPage();
        var transferPage = new TransferPage();

        var authInfo = DataHelper.getAuthInfo();

        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));
        dashboardPage.transferToFirst();
        transferPage.toThird(amount);
        transferPage.getError();
    }
}
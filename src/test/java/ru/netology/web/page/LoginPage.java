package ru.netology.web.page;

import ru.netology.web.data.DataHelper;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$x;

public class LoginPage {

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        $x("//*[@data-test-id='login']//input").setValue(info.getLogin());
        $x("//*[@data-test-id='password']//input").setValue(info.getPassword());
        $x("//*[@data-test-id='action-login']").click();
        return new VerificationPage();
    }
}
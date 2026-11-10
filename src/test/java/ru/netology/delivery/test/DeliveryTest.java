package ru.netology.delivery.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;
import ru.netology.delivery.data.Registration;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class DeliveryTest {

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Должен планировать и перепланировать встречу")
    public void shouldSuccessfulAndReplanMeeting() {

        var validUser = Registration.generateUser("ru");
        var firstDate = DataGenerator.generateDate(4);
        var secondDate = DataGenerator.generateDate(7);

        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys("DELETE");
        $("[data-test-id='date'] input").setValue(firstDate);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] .checkbox__text").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__title").shouldBe(exactText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + firstDate)).shouldBe(visible);

        $("[data-test-id='date'] input").doubleClick().sendKeys("DELETE");
        $("[data-test-id='date'] input").setValue(secondDate);
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='replan-notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='replan-notification'] .notification__title").shouldBe(exactText("Необходимо подтверждение"), Duration.ofSeconds(15));
        $("[data-test-id='replan-notification'] .notification__content").shouldBe(text("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);

        $$("button").find(exactText("Перепланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__title").shouldBe(exactText("Успешно!"), Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + secondDate)).shouldBe(visible);
    }

    @Step
    @Test
    @DisplayName("Не должен планировать встречу на теукущую дату")
    public void shouldNoSuccessfulMeetingOnCurrentDate() {

        var validUser = Registration.generateUser("ru");
        var firstDate = DataGenerator.generateDate(0);

        open("http://localhost:9999");

        Allure.step("Заполнение полей", () -> {
            Allure.attachment("Рандомный пользователь", validUser.toString());
            $("[data-test-id='city'] input").setValue(validUser.getCity());
            $("[data-test-id='date'] input").doubleClick().sendKeys("DELETE");
            $("[data-test-id='date'] input").setValue(firstDate);
            $("[data-test-id='name'] input").setValue(validUser.getName());
            $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        });
        $("[data-test-id='agreement'] .checkbox__text").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__title").shouldBe(exactText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + firstDate)).shouldBe(visible);
    }
}

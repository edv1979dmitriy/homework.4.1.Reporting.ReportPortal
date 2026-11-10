package ru.netology.delivery.data;

import com.github.javafaker.Faker;

import java.util.Locale;

public class Registration {

    public Registration() {
    }

    public static UserInfo generateUser(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return new UserInfo(DataGenerator.generateCity(), DataGenerator.generateName(faker), DataGenerator.generatePhone(faker));
    }
}

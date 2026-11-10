package ru.netology.delivery.data;

import com.github.javafaker.Faker;

import java.time.format.DateTimeFormatter;
import java.util.Random;

import static java.time.LocalDate.now;

public class DataGenerator {

    public DataGenerator() {
    }

    public static String generateDate(int shift) {
        return now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateCity(){
        String[] cities = { "Севастополь", "Майкоп", "Горно-Алтайск", "Уфа", "Улан-Удэ", "Магас", "Нальчик", "Элиста", "Черкесск", "Петрозаводск" };
        return cities[new Random().nextInt(cities.length)];
    }

    public static String generateName(Faker faker) {
        var name = faker.name().lastName() + " " + faker.name().firstName();
//        if (name.contains("ё")) {
//            name = faker.name().lastName() + " " + faker.name().firstName();
//        }
        return name;
    }

    public static String generatePhone(Faker faker) {
        return faker.phoneNumber().phoneNumber();
    }
}

package ru.pfr.global;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    // Приватный конструктор для предотвращения создания экземпляров утилитного класса
    private DateUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Формат для преобразования "dd.MM.yyyy"
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    // Формат для преобразования "yyyy-MM-dd"
    private static final DateTimeFormatter DATE_FORMATTER_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Преобразует строку в LocalDateTime в формате "dd.MM.yyyy".
     *
     * @param dateStr строка даты в формате "dd.MM.yyyy"
     * @return LocalDateTime
     */
    public static LocalDateTime parseToDate(String dateStr) {
        return LocalDateTime.of(LocalDate.parse(dateStr, DATE_FORMATTER), LocalTime.now());
    }

    /**
     * Преобразует LocalDateTime в строку в формате "dd.MM.yyyy".
     *
     * @param date LocalDateTime
     * @return строка даты в формате "dd.MM.yyyy"
     */
    public static String formatToString(LocalDateTime date) {
        return date.format(DATE_FORMATTER);
    }

    /**
     * Преобразует строку в LocalDateTime в формате "yyyy-MM-dd".
     *
     * @param dateStr строка даты в формате "yyyy-MM-dd"
     * @return LocalDateTime
     */
    public static LocalDateTime parseIsoToDate(String dateStr) {
        return LocalDateTime.of(LocalDate.parse(dateStr, DATE_FORMATTER_ISO), LocalTime.now());
    }

    /**
     * Преобразует LocalDateTime в строку в формате "yyyy-MM-dd".
     *
     * @param date LocalDateTime
     * @return строка даты в формате "yyyy-MM-dd"
     */
    public static String formatIsoToString(LocalDateTime date) {
        return date.format(DATE_FORMATTER_ISO);
    }
}
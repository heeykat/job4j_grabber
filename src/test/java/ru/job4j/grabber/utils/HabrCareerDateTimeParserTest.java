package ru.job4j.grabber.utils;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class HabrCareerDateTimeParserTest {

    @Test
    public void whenParseThenCorrectLocalDateTime() {
        var parser = new HabrCareerDateTimeParser();
        LocalDateTime result = parser.parse("2025-06-19T17:46:53+03:00");
        LocalDateTime expected = LocalDateTime.of(2025, 6, 19, 17, 46, 53);
        assertEquals(expected, result);
    }
}
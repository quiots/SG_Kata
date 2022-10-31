package org.squiot.bank.writer;

import org.itsallcode.io.Capturable;
import org.itsallcode.junit.sysextensions.SystemOutGuard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SystemOutGuard.class)
class ConsoleStatementWriterTest {

    private ConsoleStatementWriter consoleWriterStatement;

    @BeforeEach
    void init() {
        consoleWriterStatement = new ConsoleStatementWriter();
    }

    @Test
    void shouldDisplayStringListReceived(Capturable stream) {
        final List<String> operations = List.of(
                "|WITHDRAWAL                              2022-10-27 10:47:50                     100.00                                  250.00                                  |",
                "|DEPOSIT                                 2022-10-27 10:47:29                     350.00                                  350.00                                  |"
        );

        final String expectedResult = Stream.of(
                "|WITHDRAWAL                              2022-10-27 10:47:50                     100.00                                  250.00                                  |",
                "|DEPOSIT                                 2022-10-27 10:47:29                     350.00                                  350.00                                  |"
        ).collect(Collectors.joining(System.lineSeparator()));

        stream.capture();
        consoleWriterStatement.write(operations);

        final String text = stream.getCapturedData();

        assertEquals(expectedResult, text.trim());
    }

    @Test
    void shouldDisplayNothing(final Capturable stream) {
        stream.capture();
        consoleWriterStatement.write(List.of());

        final String text = stream.getCapturedData();

        assertEquals("", text.trim());
    }

}
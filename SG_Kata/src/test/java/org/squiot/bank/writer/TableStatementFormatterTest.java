package org.squiot.bank.writer;

import org.junit.jupiter.api.Test;
import org.squiot.bank.account.AccountStatement;
import org.squiot.bank.exception.NegativeAmountException;
import org.squiot.bank.operation.Operation;
import org.squiot.bank.operation.OperationType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TableStatementFormatterTest {

    @Test
    void shouldReturnConformListOfStatementInformations() throws NegativeAmountException {
        final var accountId = UUID.randomUUID();
        final var now = LocalDateTime.now();
        final var operations = List.of(
                new Operation(
                        OperationType.WITHDRAWAL,
                        accountId,
                        BigDecimal.valueOf(100).setScale(2,RoundingMode.HALF_EVEN),
                        LocalDateTime.of(2022,10,27,10,47,50),
                        BigDecimal.valueOf(250).setScale(2, RoundingMode.HALF_EVEN)
                ),
                new Operation(
                        OperationType.DEPOSIT,
                        accountId,
                        BigDecimal.valueOf(350).setScale(2,RoundingMode.HALF_EVEN),
                        LocalDateTime.of(2022,10,27,10,47,29),
                        BigDecimal.valueOf(350).setScale(2, RoundingMode.HALF_EVEN)
                )
        );

        final var accountStatement = new AccountStatement(accountId,now,operations,operations.get(0).balance());

        final var expectedFormattedResult = List.of(
                "*----------------------------------------------------------------------------------------------------------------------------------------------------------------*",
                "|                                                                       ACCOUNT STATEMENT                                                                        |",
                "|                                                                    AT : %s                                                                    |"
                        .formatted(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                "|                                                       ACCOUNT ID : %s                                                        |"
                        .formatted(accountId) ,
                "|                                                                        BALANCE : 250.00                                                                        |",
                "*----------------------------------------------------------------------------------------------------------------------------------------------------------------*",
                "|OPERATION                               DATE                                    AMOUNT                                  BALANCE                                 |",
                "*----------------------------------------------------------------------------------------------------------------------------------------------------------------*",
                "|WITHDRAWAL                              2022-10-27 10:47:50                     100.00                                  250.00                                  |",
                "|DEPOSIT                                 2022-10-27 10:47:29                     350.00                                  350.00                                  |",
                "*----------------------------------------------------------------------------------------------------------------------------------------------------------------*"
        );

        final var formattedStatement = new TableStatementFormatter().formatStatement(accountStatement);
        assertEquals(expectedFormattedResult,formattedStatement);
    }

    @Test
    void shouldReturnAccountStatementInformationsWithoutOperation() {
        final var accountId = UUID.randomUUID();
        final var now = LocalDateTime.now();
        final var accountStatement = new AccountStatement(accountId,now, Collections.emptyList(),BigDecimal.ZERO);

        final var expectedFormattedResult = List.of(
                "*----------------------------------------------------------------------------------------------------------------------------------------------------------------*",
                "|                                                                       ACCOUNT STATEMENT                                                                        |",
                "|                                                                    AT : %s                                                                    |"
                        .formatted(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                "|                                                       ACCOUNT ID : %s                                                        |"
                        .formatted(accountId) ,
                "|                                                                          BALANCE : 0                                                                           |",
                "*----------------------------------------------------------------------------------------------------------------------------------------------------------------*",
                "|OPERATION                               DATE                                    AMOUNT                                  BALANCE                                 |",
                "*----------------------------------------------------------------------------------------------------------------------------------------------------------------*",
                "|                                                                      -- NO OPERATIONS --                                                                       |",
                "*----------------------------------------------------------------------------------------------------------------------------------------------------------------*"
        );

        final var formattedStatement = new TableStatementFormatter().formatStatement(accountStatement);
        assertEquals(expectedFormattedResult,formattedStatement);
    }

}
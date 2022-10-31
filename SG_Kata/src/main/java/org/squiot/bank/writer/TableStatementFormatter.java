package org.squiot.bank.writer;

import org.squiot.bank.account.AccountStatement;
import org.squiot.bank.operation.Operation;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class TableStatementFormatter implements StatementFormatter {

    private static final int WIDTH_TABLE = 160;
    private static final String LINE_SEPARATOR = "*" + "-".repeat(WIDTH_TABLE) + "*";

    private static final List<String> HEADERS_COLUMNS = List.of(
            "OPERATION",
            "DATE",
            "AMOUNT",
            "BALANCE"
    );

    @Override
    public List<String> formatStatement(AccountStatement accountStatement) {
        return Stream.of(
                        formatInformationsHeader(accountStatement),
                        formatColumnsHeader(),
                        formatOperations(accountStatement)
                )
                .flatMap(Collection::stream)
                .toList();
    }

    private static List<String> formatInformationsHeader(AccountStatement accountStatement) {
        String title = "|" + centerString(WIDTH_TABLE, "ACCOUNT STATEMENT") + "|";
        String at = "|" + centerString(WIDTH_TABLE, "AT : " + accountStatement.date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) + "|";
        String id = "|" + centerString(WIDTH_TABLE, "ACCOUNT ID : " + accountStatement.accountId()) + "|";
        String balance = "|" + centerString(WIDTH_TABLE, "BALANCE : " + accountStatement.balance()) + "|";

        return List.of(
                LINE_SEPARATOR,
                title,
                at,
                id,
                balance,
                LINE_SEPARATOR
        );
    }

    private static List<String> formatColumnsHeader() {
        StringBuilder headerColumns = new StringBuilder();

        headerColumns.append("|");

        for (String column : HEADERS_COLUMNS) {
            String columnResponsive = column
                    + " ".repeat(WIDTH_TABLE / HEADERS_COLUMNS.size() - column.length());
            headerColumns.append(columnResponsive);
        }

        headerColumns.append("|");

        return List.of(
                headerColumns.toString(),
                LINE_SEPARATOR
        );
    }

    private static String formatLineOperation(Operation operation) {

        return "|" +
                rightSpacedString(WIDTH_TABLE, HEADERS_COLUMNS.size(), operation.operationType().toString()) +
                rightSpacedString(WIDTH_TABLE, HEADERS_COLUMNS.size(), operation.date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) +
                rightSpacedString(WIDTH_TABLE, HEADERS_COLUMNS.size(), operation.amount().toString()) +
                rightSpacedString(WIDTH_TABLE, HEADERS_COLUMNS.size(), operation.balance().toString()) +
                "|";
    }

    private static List<String> formatOperations(AccountStatement accountStatement) {
        List<String> operations = new ArrayList<>();

        if (accountStatement.operations().isEmpty()) {
            operations.add("|" + centerString(WIDTH_TABLE, "-- NO OPERATIONS --") + "|");
            operations.add(LINE_SEPARATOR);
            return operations;
        }

        for (Operation operation : accountStatement.operations()) {
            operations.add(formatLineOperation(operation));
        }

        operations.add(LINE_SEPARATOR);
        return operations;
    }

    private static String centerString(int width, String s) {
        return String.format("%-" + width + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }

    private static String rightSpacedString(int width, int numberString, String s) {
        return s + " ".repeat(width / numberString - s.length());
    }

}


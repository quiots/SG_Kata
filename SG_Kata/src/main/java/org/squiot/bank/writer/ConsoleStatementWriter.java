package org.squiot.bank.writer;

import java.util.List;

public class ConsoleStatementWriter implements StatementWriter {
    @Override
    public void write(List<String> statementLines) {
        for (String statementLine : statementLines) {
            System.out.println(statementLine);
        }
    }
}

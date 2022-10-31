package org.squiot.bank.writer;

import java.util.List;

public interface StatementWriter {
    void write(List<String> statementDetails);
}

package org.squiot.bank.writer;

import org.squiot.bank.account.AccountStatement;

import java.util.List;

public interface StatementFormatter {
    List<String> formatStatement(AccountStatement accountStatement);
}

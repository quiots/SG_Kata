package org.squiot.bank.operation;

import org.squiot.bank.exception.InsufficientBalanceException;
import org.squiot.bank.exception.NegativeAmountException;
import org.squiot.bank.operation.data.OperationDAO;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class OperationService {

    private final OperationDAO operationDAO;
    private final Clock clock;

    public OperationService(OperationDAO operationDAO, Clock clock) {
        this.operationDAO = operationDAO;
        this.clock = clock;
    }

    public Operation deposit(UUID accountId, BigDecimal amount) throws NegativeAmountException {
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new NegativeAmountException("Amount's value shouldn't be negative.");

        final BigDecimal balance = getBalanceFromLastOperationByAccountId(accountId);
        final BigDecimal updatedBalance = balance.add(amount);
        final Operation operation = new Operation(OperationType.DEPOSIT, accountId, amount, LocalDateTime.now(clock), updatedBalance);

        return operationDAO.create(operation);

    }


    public Operation withdrawal(UUID accountId, BigDecimal amount) throws InsufficientBalanceException, NegativeAmountException {
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new NegativeAmountException("Amount's value shouldn't be negative.");

        final BigDecimal balance = getBalanceFromLastOperationByAccountId(accountId);

        if (balance.compareTo(amount) < 0)
            throw new InsufficientBalanceException("Balance is insufficient for this withdrawal.");

        final BigDecimal updatedBalance = balance.subtract(amount);
        final Operation operation = new Operation(OperationType.WITHDRAWAL, accountId, amount, LocalDateTime.now(clock), updatedBalance);
        return operationDAO.create(operation);

    }

    private BigDecimal getBalanceFromLastOperationByAccountId(UUID accountId) {
        return operationDAO.findLastOperationByAccountId(accountId).map(Operation::balance)
                .orElse(BigDecimal.ZERO);
    }
}
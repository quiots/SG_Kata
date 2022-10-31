package org.squiot.bank.account;

import org.squiot.bank.operation.Operation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AccountStatement(UUID accountId, LocalDateTime date, List<Operation> operations, BigDecimal balance) { }


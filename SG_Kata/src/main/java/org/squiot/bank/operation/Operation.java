package org.squiot.bank.operation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Operation(OperationType operationType, UUID accountId,BigDecimal amount, LocalDateTime date, BigDecimal balance) { }

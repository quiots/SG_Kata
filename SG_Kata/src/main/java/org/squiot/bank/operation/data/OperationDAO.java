package org.squiot.bank.operation.data;

import org.squiot.bank.operation.Operation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperationDAO {
    Optional<Operation> findLastOperationByAccountId(UUID accountId);
    List<Operation> findAllSortedOperationsByAccountId(UUID accountId);
    Operation create(Operation operation);
}

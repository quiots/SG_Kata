package org.squiot.bank.operation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.squiot.bank.exception.NegativeAmountException;
import org.squiot.bank.operation.data.OperationDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

    @Mock
    private OperationDAO operationDAO;

    private OperationService operationService;

    private final Clock clock = Clock.fixed(Instant.parse("2022-10-28T16:08:00.00Z"), ZoneOffset.UTC);

    @BeforeEach
    void initOperationService(){
        operationService = new OperationService(operationDAO, clock);
    }

    @Test
    @DisplayName("should deposit a correct amount value on an account without any operation")
    void shouldDepositAmountOnAccountWithoutLastOperation() throws NegativeAmountException{
        final LocalDateTime date = LocalDateTime.now(clock);
        final UUID accountId = UUID.randomUUID();
        final BigDecimal expectedAmountValue = BigDecimal.valueOf(1532).setScale(2, RoundingMode.HALF_EVEN);
        final Operation expectedOperation = new Operation(
                OperationType.DEPOSIT,
                accountId,
                expectedAmountValue,
                date,
                BigDecimal.valueOf(1532).setScale(2, RoundingMode.HALF_EVEN)
        );
        when(operationDAO.create(expectedOperation))
                .thenReturn(expectedOperation);

        final Operation returnedOperation = operationService.deposit(accountId,expectedAmountValue);

        assertEquals(expectedOperation,returnedOperation);
        final InOrder orderVerifier = inOrder(operationDAO);
        orderVerifier.verify(operationDAO).findLastOperationByAccountId(accountId);
        orderVerifier.verify(operationDAO).create(expectedOperation);
        orderVerifier.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("should deposit a correct amount value with existing previous operation")
    void shouldDepositAmountOnAccountWithPreviousOperation() throws NegativeAmountException{
        final LocalDateTime date = LocalDateTime.now(clock);
        final UUID accountId = UUID.randomUUID();
        final BigDecimal expectedAmountValue = BigDecimal.valueOf(100).setScale(2,RoundingMode.HALF_EVEN);
        final Operation expectedOperation = new Operation(
                OperationType.DEPOSIT,
                accountId,
                expectedAmountValue,
                date,
                BigDecimal.valueOf(250).setScale(2, RoundingMode.HALF_EVEN)
        );

        when(operationDAO.findLastOperationByAccountId(accountId))
                .thenReturn(
                        Optional.of(new Operation(
                                OperationType.DEPOSIT,
                                accountId,
                                BigDecimal.valueOf(150).setScale(2,RoundingMode.HALF_EVEN),
                                date,
                                BigDecimal.valueOf(150).setScale(2, RoundingMode.HALF_EVEN)
                        ))
                );

        when(operationDAO.create(expectedOperation)).thenReturn(expectedOperation);

        final Operation returnedOperation = operationService.deposit(accountId,expectedAmountValue);

        assertEquals(expectedOperation,returnedOperation);
        final InOrder orderVerifier = inOrder(operationDAO);
        orderVerifier.verify(operationDAO).findLastOperationByAccountId(accountId);
        orderVerifier.verify(operationDAO).create(expectedOperation);
        orderVerifier.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("should throw negative amount exception when deposit negative amount")
    void shouldThrowNegativeAmountExceptionWhenDepositNegativeAmount(){
        final LocalDateTime date = LocalDateTime.now(clock);
        final UUID accountId = UUID.randomUUID();
        final BigDecimal expectedAmountValue = BigDecimal.valueOf(-100);

        assertThrows(NegativeAmountException.class,()-> operationService.deposit(accountId,expectedAmountValue));
    }
}
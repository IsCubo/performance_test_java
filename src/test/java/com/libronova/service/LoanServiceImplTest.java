/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libronova.service;

import com.mycompany.libronova.dao.BookDAO;
import com.mycompany.libronova.dao.LoanDAO;
import com.mycompany.libronova.dao.MemberDAO;
import com.mycompany.libronova.exception.InsufficientStockException;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.model.Loan;
import com.mycompany.libronova.model.Member;
import com.mycompany.libronova.model.enums.MemberStatus;
import com.mycompany.libronova.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private LoanDAO loanDAO;
    @Mock
    private BookDAO bookDAO;
    @Mock
    private MemberDAO memberDAO;

    @InjectMocks
    private LoanServiceImpl loanServiceImpl;

    private Book testBook;
    private Member testMember;
    private Loan testLoan;

    // NOTA: Para que esta prueba funcione, tu config.properties debe tener:
    // diasPrestamo=7
    // multaPorDia=1500
    // Asumiremos estos valores.

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setIsbn("9876543210987");
        testBook.setAvailableCopies(1);

        testMember = new Member();
        testMember.setId(1);
        testMember.setStatus(MemberStatus.ACTIVE);

        testLoan = new Loan();
        testLoan.setId(101);
        testLoan.setBookIsbn(testBook.getIsbn());
    }

    // --- PRUEBA DE VALIDACIÓN DE STOCK ---
    @Test
    void performLoan_should_throwStockInsuficienteException_when_bookHasZeroCopies() {
        // --- GIVEN ---
        // Preparamos un libro sin copias disponibles.
        testBook.setAvailableCopies(0);
        when(bookDAO.findByIsbn(testBook.getIsbn())).thenReturn(Optional.of(testBook));

        // --- WHEN & THEN ---
        // Verificamos que la excepción correcta es lanzada.
        assertThrows(InsufficientStockException.class, () -> {
            loanServiceImpl.performLoan(testBook.getIsbn(), testMember.getId());
        });

        // Verificamos que la transacción se detuvo y no se intentó crear el préstamo.
        verify(loanDAO, never()).create(any(Loan.class));
    }

    // --- PRUEBAS DE CÁLCULO DE MULTAS ---
    @Test
    void registerReturn_should_calculateZeroFine_when_bookIsReturnedOnTime() {
        // --- GIVEN ---
        // La fecha de devolución es hoy, la misma que la fecha límite.
        LocalDate today = LocalDate.now();
        testLoan.setDueDate(today); 
        
        when(loanDAO.findById(testLoan.getId())).thenReturn(Optional.of(testLoan));
        when(bookDAO.findByIsbn(testBook.getIsbn())).thenReturn(Optional.of(testBook));

        // Simulamos la respuesta del método update del DAO para poder hacer aserciones sobre el objeto modificado.
        when(loanDAO.update(any(Loan.class), any())).thenAnswer(invocation -> invocation.getArgument(0));

        // --- WHEN ---
        Loan returnedLoan = loanServiceImpl.registerReturn(testLoan.getId());
        
        // --- THEN ---
        // assertEquals se usa para comparar valores. Esperamos que la multa sea 0.0.
        assertEquals(0.0, returnedLoan.getFine(), "The fine should be 0.0 for on-time returns.");
        // assertTrue se usa para verificar una condición booleana.
        assertTrue(returnedLoan.isReturned(), "The loan should be marked as returned.");
    }

    @Test
    void registerReturn_should_calculateCorrectFine_when_bookIsReturnedLate() {
        // --- GIVEN ---
        // El libro debía ser devuelto hace 5 días.
        LocalDate dueDate = LocalDate.now().minusDays(5);
        testLoan.setDueDate(dueDate);
        
        when(loanDAO.findById(testLoan.getId())).thenReturn(Optional.of(testLoan));
        when(bookDAO.findByIsbn(testBook.getIsbn())).thenReturn(Optional.of(testBook));
        when(loanDAO.update(any(Loan.class), any())).thenAnswer(invocation -> invocation.getArgument(0));
        
        // --- WHEN ---
        Loan returnedLoan = loanServiceImpl.registerReturn(testLoan.getId());

        // --- THEN ---
        double expectedFine = 5 * 1500.0; // 5 días de retraso * 1500 por día
        assertEquals(expectedFine, returnedLoan.getFine(), "The fine for 5 overdue days should be calculated correctly.");
        assertTrue(returnedLoan.isReturned());
    }
}

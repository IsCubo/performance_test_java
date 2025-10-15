package com.libronova.service; // Usa tu estructura de paquetes


import com.mycompany.libronova.dao.BookDAO;
import com.mycompany.libronova.exception.IsbnDuplicateException;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita la integración de Mockito con JUnit 5
class BookServiceImplTest {

    @Mock // Crea un mock (un doble de prueba) del BookDAO. No usará la base de datos real.
    private BookDAO bookDAO;

    @InjectMocks // Crea una instancia de BookServiceImpl e inyecta el mock de BookDAO en ella.
    private BookServiceImpl bookServiceImpl;

    private Book testBook;

    @BeforeEach
    void setUp() {
        // Prepara un objeto de prueba que usaremos en varios tests
        testBook = new Book();
        testBook.setIsbn("1234567890123");
        testBook.setTitle("A Test Book");
        testBook.setAuthor("Test Author");
        testBook.setTotalCopies(5);
    }

    @Test
    void registerBook_should_throwIsbnDuplicadoException_when_isbnAlreadyExists() {
        // --- GIVEN (Dado que...) ---
        // Le decimos al mock que cuando se llame a findByIsbn con nuestro ISBN de prueba,
        // debe simular que ya encontró un libro en la base de datos.
        when(bookDAO.findByIsbn(testBook.getIsbn())).thenReturn(Optional.of(new Book()));

        // --- WHEN (Cuando...) & THEN (Entonces...) ---
        // Verificamos que al llamar a registerBook, se lanza la excepción que esperamos.
        // assertThrows es la forma correcta de probar excepciones en JUnit 5.
        assertThrows(IsbnDuplicateException.class, () -> {
            bookServiceImpl.registerBook(testBook);
        });

        // Adicionalmente, verificamos que el método create del DAO NUNCA fue llamado,
        // asegurando que no se intentó guardar el duplicado.
        verify(bookDAO, never()).create(any(Book.class));
    }

    @Test
    void registerBook_should_succeed_when_isbnIsUnique() {
        // --- GIVEN (Dado que...) ---
        // Simulamos el caso "feliz": cuando se busca el ISBN, no se encuentra nada.
        when(bookDAO.findByIsbn(testBook.getIsbn())).thenReturn(Optional.empty());

        // --- WHEN (Cuando...) ---
        // Llamamos al método que estamos probando.
        bookServiceImpl.registerBook(testBook);

        // --- THEN (Entonces...) ---
        // Verificamos que el método create del DAO SÍ fue llamado exactamente una vez.
        // Esto confirma que la lógica de guardado procedió como se esperaba.
        verify(bookDAO, times(1)).create(testBook);
    }
}
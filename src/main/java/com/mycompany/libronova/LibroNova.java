/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.libronova;

import com.mycompany.libronova.controller.*;
import com.mycompany.libronova.dao.*;
import com.mycompany.libronova.dao.impl.*;
import com.mycompany.libronova.service.*;
import com.mycompany.libronova.service.decorator.UserWithDefaultsDecorator;
import com.mycompany.libronova.service.impl.*;
import com.mycompany.libronova.view.LoginView;
import com.mycompany.libronova.view.MainView;
import javax.swing.SwingUtilities;

/**
 *
 * @author Coder
 */
public class LibroNova {

    public static void main(String[] args) {
        // --- INYECCIÓN DE DEPENDENCIAS ---

        // --- DAOs ---
        BookDAO bookDAO = new BookDAOImpl();
        LoanDAO loanDAO = new LoanDAOImpl();
        MemberDAO memberDAO = new MemberDAOImpl();
        UserDAO userDAO = new UserDAOImpl();

        // --- Services ---
        AuthService authService = new AuthServiceImpl(userDAO);
        BookService bookService = new BookServiceImpl(bookDAO);
        MemberService memberService = new MemberServiceImpl(memberDAO); // Necesitarás crear esta clase
        LoanService loanService = new LoanServiceImpl(loanDAO, bookDAO, memberDAO);
        ExportService exportService = new ExportServiceImpl(bookDAO, loanDAO);
        
        // Decorator 
        UserService baseUserService = new UserServiceImpl(userDAO);
        UserService userService = new UserWithDefaultsDecorator(baseUserService);

        // --- Controllers ---
        AuthController authController = new AuthController(authService);
        BookController bookController = new BookController(bookService);
        MemberController memberController = new MemberController(memberService);
        UserController userController = new UserController(userService);
        LoanController loanController = new LoanController(loanService);
        ExportController exportController = new ExportController(exportService);

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView(authController);
            
            loginView.setLoginSuccessListener(userOptional -> {
                if (userOptional.isPresent()) {
                    
                    // Crete and show main window
                    MainView mainView = new MainView(
                        userOptional.get(), 
                        bookController, 
                        memberController, 
                        userController, 
                        exportController, 
                        loanController
                    );
                    mainView.setVisible(true);
                }
            });
            
            loginView.setVisible(true);
        });
    }
}

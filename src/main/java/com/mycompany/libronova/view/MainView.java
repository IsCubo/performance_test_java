/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.view;

import com.mycompany.libronova.controller.BookController;
import com.mycompany.libronova.controller.ExportController;
import com.mycompany.libronova.controller.LoanController;
import com.mycompany.libronova.controller.MemberController;
import com.mycompany.libronova.controller.UserController;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.model.enums.UserRole;
import com.mycompany.libronova.view.panels.BookManagementPanel;
import com.mycompany.libronova.view.panels.LoanManagementPanel;
import com.mycompany.libronova.view.panels.MemberManagementPanel;
import com.mycompany.libronova.view.panels.UserManagementPanel;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Coder
 */
public class MainView extends JFrame {

    private final BookController bookController;
    private final MemberController memberController;
    private final UserController userController;
    private final ExportController exportController;
    private final LoanController loanController;

    private final User loggedInUser;

    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    public MainView(User loggedInUser, BookController bookController, MemberController memberController, UserController userController, ExportController exportController, LoanController loanController) {
        // Primero, se asignan todas las variables final
        this.loggedInUser = loggedInUser;
        this.bookController = bookController;
        this.memberController = memberController;
        this.userController = userController;
        this.exportController = exportController;
        this.loanController = loanController;

        // Luego, se configuran los componentes
        setTitle("LibroNova Management System - User: " + loggedInUser.getUsername() + " (" + loggedInUser.getRole() + ")");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponentsManual();
    }

    private void initComponentsManual() {
        setJMenuBar(createMenuBar());

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.add(new JLabel("Welcome to LibroNova! Select an option from the menu to begin."));

        BookManagementPanel bookPanel = new BookManagementPanel(bookController);
        MemberManagementPanel memberPanel = new MemberManagementPanel(memberController);
        UserManagementPanel userPanel = new UserManagementPanel(userController);
        LoanManagementPanel loanPanel = new LoanManagementPanel(loggedInUser, loanController, bookController, memberController);

        mainContentPanel.add(welcomePanel, "WELCOME");
        mainContentPanel.add(bookPanel, "BOOKS");
        mainContentPanel.add(memberPanel, "MEMBERS");
        mainContentPanel.add(userPanel, "USERS");
        mainContentPanel.add(loanPanel, "LOANS");

        add(mainContentPanel, BorderLayout.CENTER);
        cardLayout.show(mainContentPanel, "WELCOME");
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu exportSubMenu = new JMenu("Export");
        JMenuItem exportBooksItem = new JMenuItem("Export Book Catalog...");
        exportBooksItem.addActionListener(e -> exportController.exportBooks());
        JMenuItem exportOverdueLoansItem = new JMenuItem("Export Overdue Loans...");
        exportOverdueLoansItem.addActionListener(e -> exportController.exportOverdueLoans());
        exportSubMenu.add(exportBooksItem);
        exportSubMenu.add(exportOverdueLoansItem);
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exportSubMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu managementMenu = new JMenu("Management");
        JMenuItem manageBooksItem = new JMenuItem("Manage Books");
        manageBooksItem.addActionListener(e -> cardLayout.show(mainContentPanel, "BOOKS"));
        JMenuItem manageMembersItem = new JMenuItem("Manage Members");
        manageMembersItem.addActionListener(e -> cardLayout.show(mainContentPanel, "MEMBERS"));
        JMenuItem manageUsersItem = new JMenuItem("Manage Users");
        manageUsersItem.addActionListener(e -> cardLayout.show(mainContentPanel, "USERS"));
        menuBar.add(fileMenu);
        if (loggedInUser.getRole() == UserRole.ADMIN) {
            menuBar.add(managementMenu);
            manageUsersItem.setEnabled(true);
            managementMenu.add(manageUsersItem);
            menuBar.add(managementMenu);
            managementMenu.add(manageBooksItem);
            managementMenu.add(manageMembersItem);
        }

        JMenu loansMenu = new JMenu("Loans");
        JMenuItem manageLoansItem = new JMenuItem("Manage Loans & Returns");
        manageLoansItem.addActionListener(e -> cardLayout.show(mainContentPanel, "LOANS"));
        loansMenu.add(manageLoansItem);

        menuBar.add(loansMenu);
        return menuBar;
    }
}

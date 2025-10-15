package com.mycompany.libronova.view.panels;

import com.mycompany.libronova.controller.BookController;
import com.mycompany.libronova.controller.LoanController;
import com.mycompany.libronova.controller.MemberController;
import com.mycompany.libronova.model.Book;
import com.mycompany.libronova.model.Loan;
import com.mycompany.libronova.model.Member;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.model.enums.UserRole;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LoanManagementPanel extends JPanel {

    private final User loggedInUser;
    private final LoanController loanController;
    private final BookController bookController;
    private final MemberController memberController;

    // --- Componentes Visuales ---
    private JComboBox<Member> memberComboBox;
    private JComboBox<Book> bookComboBox;
    private JButton processLoanButton, registerReturnButton;
    private JTable loanTable;
    private DefaultTableModel tableModel;

    // --- Componentes para la búsqueda de asistentes ---
    private JComboBox<Member> searchMemberComboBox;
    private JButton findLoansButton;

    public LoanManagementPanel(User loggedInUser, LoanController loanController, BookController bookController, MemberController memberController) {
        this.loggedInUser = loggedInUser;
        this.loanController = loanController;
        this.bookController = bookController;
        this.memberController = memberController;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        initListeners();
        refreshComboBoxes(); // Carga los ComboBox

        // Carga inicial de la tabla según el rol
        if (loggedInUser.getRole() == UserRole.ADMIN) {
            loadAllLoans();
        }
        // Para asistentes, la tabla empieza vacía.
    }

    private void initComponents() {
        // Contenedor superior para los paneles de acción (Nuevo Préstamo y Búsqueda)
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        // --- Panel 1: Nuevo Préstamo (Visible para todos) ---
        JPanel newLoanPanel = createNewLoanPanel();
        topContainer.add(newLoanPanel);

        // --- Panel 2: Búsqueda por Socio (Visible SOLO para Asistentes) ---
        if (loggedInUser.getRole() != UserRole.ADMIN) {
            JPanel searchPanel = createSearchPanel();
            topContainer.add(searchPanel);
        }

        add(topContainer, BorderLayout.NORTH);

        // --- Panel Central: Lista de Préstamos y Devoluciones ---
        JPanel loanListPanel = createLoanListPanel();
        add(loanListPanel, BorderLayout.CENTER);
    }
    
    // --- MÉTODOS DE AYUDA PARA LA CONSTRUCCIÓN DE LA UI ---
    
    private JPanel createNewLoanPanel() {
        JPanel newLoanPanel = new JPanel(new GridBagLayout());
        newLoanPanel.setBorder(BorderFactory.createTitledBorder(null, "New Loan", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", 1, 12)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        newLoanPanel.add(new JLabel("Select Member:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.9;
        memberComboBox = new JComboBox<>();
        memberComboBox.setRenderer(new MemberComboBoxRenderer());
        newLoanPanel.add(memberComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        newLoanPanel.add(new JLabel("Select Book:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        bookComboBox = new JComboBox<>();
        bookComboBox.setRenderer(new BookComboBoxRenderer());
        newLoanPanel.add(bookComboBox, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        processLoanButton = new JButton("Process New Loan");
        newLoanPanel.add(processLoanButton, gbc);
        return newLoanPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder(null, "Find Member's Loans", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", 1, 12)));
        searchPanel.add(new JLabel("Select a Member:"));
        searchMemberComboBox = new JComboBox<>();
        searchMemberComboBox.setRenderer(new MemberComboBoxRenderer());
        searchPanel.add(searchMemberComboBox);
        findLoansButton = new JButton("Find Loans");
        searchPanel.add(findLoansButton);
        return searchPanel;
    }

    private JPanel createLoanListPanel() {
        JPanel loanListPanel = new JPanel(new BorderLayout(10, 10));
        loanListPanel.setBorder(BorderFactory.createTitledBorder(null, "Loan History & Returns", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", 1, 12)));

        String[] columnNames = {"Loan ID", "Book ISBN", "Member ID", "Loan Date", "Due Date", "Return Date", "Fine", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        loanTable = new JTable(tableModel);
        loanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(loanTable);
        loanListPanel.add(scrollPane, BorderLayout.CENTER);

        registerReturnButton = new JButton("Register Return for Selected Loan");
        loanListPanel.add(registerReturnButton, BorderLayout.SOUTH);
        return loanListPanel;
    }

    private void initListeners() {
        processLoanButton.addActionListener(e -> processNewLoan());
        registerReturnButton.addActionListener(e -> registerReturn());
        
        // El listener para la búsqueda solo se añade si el botón existe (es decir, si el usuario es Asistente)
        if (loggedInUser.getRole() != UserRole.ADMIN) {
            findLoansButton.addActionListener(e -> {
                Member selectedMember = (Member) searchMemberComboBox.getSelectedItem();
                if (selectedMember != null) {
                    loadLoansForMember(selectedMember.getId());
                }
            });
        }
    }

    // --- MÉTODOS DE LÓGICA DE ACCIÓN ---

    private void processNewLoan() {
        Member selectedMember = (Member) memberComboBox.getSelectedItem();
        Book selectedBook = (Book) bookComboBox.getSelectedItem();

        if (selectedMember == null || selectedBook == null) {
            JOptionPane.showMessageDialog(this, "Please select a member and a book.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (loanController.performLoan(selectedBook.getIsbn(), selectedMember.getId())) {
            // Refrescar todo para mostrar el nuevo estado
            refreshComboBoxes();
            if (loggedInUser.getRole() == UserRole.ADMIN) {
                loadAllLoans();
            } else {
                // Para el asistente, recargamos los préstamos del socio que acabamos de afectar.
                loadLoansForMember(selectedMember.getId());
            }
        }
    }

    private void registerReturn() {
        int selectedRow = loanTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a loan from the table to register its return.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int loanId = (int) tableModel.getValueAt(selectedRow, 0);
        int memberId = (int) tableModel.getValueAt(selectedRow, 2); // Necesitamos el ID del socio para refrescar
        String status = (String) tableModel.getValueAt(selectedRow, 7);

        if (!"ACTIVE".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(this, "This loan has already been returned.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (loanController.registerReturn(loanId)) {
            // Refrescar todo para mostrar el nuevo estado
            refreshComboBoxes();
            if (loggedInUser.getRole() == UserRole.ADMIN) {
                loadAllLoans();
            } else {
                // Para el asistente, recargamos los préstamos del socio que acabamos de afectar.
                loadLoansForMember(memberId);
            }
        }
    }

    // --- MÉTODOS DE CARGA DE DATOS ---

    private void refreshComboBoxes() {
        memberComboBox.removeAllItems();
        List<Member> activeMembers = memberController.getAllActiveMembers();
        activeMembers.forEach(memberComboBox::addItem);

        bookComboBox.removeAllItems();
        List<Book> availableBooks = bookController.getAllAvailableBooks();
        availableBooks.forEach(bookComboBox::addItem);

        // Si es asistente, también recarga el ComboBox de búsqueda
        if (loggedInUser.getRole() != UserRole.ADMIN) {
            searchMemberComboBox.removeAllItems();
            activeMembers.forEach(searchMemberComboBox::addItem); // Reutilizamos la misma lista de socios activos
        }
    }
    
    private void loadAllLoans() {
        List<Loan> allLoans = loanController.getAllLoans();
        populateTable(allLoans);
    }

    private void loadLoansForMember(int memberId) {
        List<Loan> memberLoans = loanController.getLoansByMemberId(memberId);
        populateTable(memberLoans);
    }
    
    private void populateTable(List<Loan> loans) {
        tableModel.setRowCount(0);
        for (Loan loan : loans) {
            Object[] rowData = {
                loan.getId(), loan.getBookIsbn(), loan.getMemberId(),
                loan.getLoanDate(), loan.getDueDate(),
                loan.getReturnDate() == null ? "N/A" : loan.getReturnDate(),
                String.format("%.2f", loan.getFine()),
                loan.isReturned() ? "RETURNED" : "ACTIVE"
            };
            tableModel.addRow(rowData);
        }
    }

    // --- Clases Internas para renderizar los ComboBox de forma legible ---
    private static class MemberComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Member) {
                Member member = (Member) value;
                setText(member.getId() + ": " + member.getFirstName() + " " + member.getLastName());
            }
            return this;
        }
    }

    private static class BookComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Book) {
                Book book = (Book) value;
                setText(book.getTitle() + " (by " + book.getAuthor() + ")");
            }
            return this;
        }
    }
}
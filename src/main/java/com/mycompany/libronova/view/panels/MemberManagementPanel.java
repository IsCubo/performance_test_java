/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.view.panels;

/**
 *
 * @author Coder
 */
import com.mycompany.libronova.controller.MemberController;
import com.mycompany.libronova.model.Member;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * A JPanel for managing library members. It displays member information
 * in a table and provides controls for adding and editing them.
 */
public class MemberManagementPanel extends JPanel {

    private final MemberController memberController;

    // --- UI Components ---
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton;
    private JTextField searchField;

    public MemberManagementPanel(MemberController memberController) {
        this.memberController = memberController;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        initListeners();
        
        // Initial data load
        loadMemberData();
    }

    private void initComponents() {
        // --- Top Panel (Toolbar) ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.add(new JLabel("Search by Name/Email:"));
        searchField = new JTextField(25);
        topPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel (Table) ---
        String[] columnNames = {"ID", "First Name", "Last Name", "Email", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        memberTable = new JTable(tableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(memberTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- Right Panel (Action Buttons) ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        addButton = new JButton("Add New Member");
        editButton = new JButton("Edit Selected");
        
        Dimension buttonSize = new Dimension(140, 30);
        addButton.setPreferredSize(buttonSize);
        editButton.setPreferredSize(buttonSize);
        addButton.setMaximumSize(buttonSize);
        editButton.setMaximumSize(buttonSize);
        
        rightPanel.add(addButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(editButton);
        
        add(rightPanel, BorderLayout.EAST);
    }

    private void initListeners() {
        addButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Add New Member dialog would open here.", "Info", JOptionPane.INFORMATION_MESSAGE);
            loadMemberData();
        });

        editButton.addActionListener(e -> {
            int selectedRow = memberTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a member to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int memberId = (int) tableModel.getValueAt(selectedRow, 0);
            JOptionPane.showMessageDialog(this, "Edit dialog for Member ID: " + memberId + " would open here.", "Info", JOptionPane.INFORMATION_MESSAGE);
            loadMemberData();
        });
    }

    private void loadMemberData() {
        tableModel.setRowCount(0);
        List<Member> members = memberController.getAllMembers();
        for (Member member : members) {
            Object[] rowData = {
                member.getId(),
                member.getFirstName(),
                member.getLastName(),
                member.getEmail(),
                member.getStatus().name()
            };
            tableModel.addRow(rowData);
        }
    }
}

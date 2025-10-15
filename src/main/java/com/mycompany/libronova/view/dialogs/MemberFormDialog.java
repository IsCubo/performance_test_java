/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.view.dialogs;

/**
 *
 * @author Coder
 */
import com.mycompany.libronova.controller.MemberController;
import com.mycompany.libronova.model.Member;
import com.mycompany.libronova.model.enums.MemberStatus;
import javax.swing.*;
import java.awt.*;

/**
 * A modal JDialog for adding or editing a library member.
 */
public class MemberFormDialog extends JDialog {

    private final MemberController memberController;
    private final boolean isEditMode;
    private Member memberToEdit;

    // --- UI Components ---
    private JTextField firstNameField, lastNameField, emailField;
    private JComboBox<MemberStatus> statusComboBox;
    private JButton saveButton, cancelButton;

    public MemberFormDialog(Frame parent, MemberController memberController) {
        super(parent, "Add New Member", true);
        this.memberController = memberController;
        this.isEditMode = false;
        initComponents();
    }
    
    public MemberFormDialog(Frame parent, MemberController memberController, Member memberToEdit) {
        super(parent, "Edit Member", true);
        this.memberController = memberController;
        this.isEditMode = true;
        this.memberToEdit = memberToEdit;
        initComponents();
        populateForm();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Rows for First Name, Last Name, Email
        gbc.gridy = 0; formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridy = 1; firstNameField = new JTextField(20); formPanel.add(firstNameField, gbc);
        gbc.gridy = 2; formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridy = 3; lastNameField = new JTextField(20); formPanel.add(lastNameField, gbc);
        gbc.gridy = 4; formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridy = 5; emailField = new JTextField(20); formPanel.add(emailField, gbc);

        // Row for Status
        gbc.gridy = 6; formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridy = 7; 
        statusComboBox = new JComboBox<>(MemberStatus.values());
        formPanel.add(statusComboBox, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        initListeners();
        pack();
        setLocationRelativeTo(getParent());
    }

    private void populateForm() {
        if (memberToEdit != null) {
            firstNameField.setText(memberToEdit.getFirstName());
            lastNameField.setText(memberToEdit.getLastName());
            emailField.setText(memberToEdit.getEmail());
            statusComboBox.setSelectedItem(memberToEdit.getStatus());
        }
    }

    private void initListeners() {
        saveButton.addActionListener(e -> saveMember());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveMember() {
        if (firstNameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name and Email are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        MemberStatus status = (MemberStatus) statusComboBox.getSelectedItem();

        if (isEditMode) {
            memberToEdit.setFirstName(firstName);
            memberToEdit.setLastName(lastName);
            memberToEdit.setEmail(email);
            memberToEdit.setStatus(status);
            // Assuming MemberController has an updateMember method
            // if (memberController.updateMember(memberToEdit)) {
            //     dispose();
            // }
            JOptionPane.showMessageDialog(this, "Update logic not yet connected.", "Info", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            Member newMember = new Member();
            newMember.setFirstName(firstName);
            newMember.setLastName(lastName);
            newMember.setEmail(email);
            newMember.setStatus(status);
            if (memberController.createMember(newMember)) {
                dispose();
            }
        }
    }
}

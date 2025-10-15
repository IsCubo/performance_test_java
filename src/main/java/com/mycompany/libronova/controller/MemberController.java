/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.controller;

import com.mycompany.libronova.exception.LibroNovaException;
import com.mycompany.libronova.model.Member;
import com.mycompany.libronova.service.MemberService;
import com.mycompany.libronova.util.LoggerManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 *
 * @author Coder
 */
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    public List<Member> getAllMembers() {
        try {
            return memberService.getAllMembers();
        } catch (LibroNovaException ex) {
            handleError(ex, "Failed to load members.");
            return Collections.emptyList();
        }
    }
    
    public boolean createMember(Member newMember) {
        try {
            memberService.registerMember(newMember);
            showSuccessMessage("Member '" + newMember.getFirstName() + "' was successfully registered.");
            return true;
        } catch (LibroNovaException ex) {
            handleError(ex, "Could not register the member.");
            return false;
        }
    }

    private void handleError(LibroNovaException ex, String logMessage) {
        LoggerManager.log(Level.SEVERE, logMessage + " Error: " + ex.getMessage(), ex);
        JOptionPane.showMessageDialog(null,
            "Operation failed: " + ex.getErrorCode().getMessage(),
            "Error - Code: " + ex.getErrorCode().getCode(),
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public Optional<Member> getMemberById(int id) {
    try {
        return memberService.getMemberById(id);
    } catch (LibroNovaException ex) {
        handleError(ex, "Failed to retrieve member with id: " + id);
        return Optional.empty();
    }
}
}

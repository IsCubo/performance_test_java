/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service;

import com.mycompany.libronova.model.Member;
import java.util.List;

/**
 *
 * @author Coder
 */
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing {@link Member} entities.
 * <p>
 * Defines the business operations for member management within the LibroNova system,
 * including registration, updates, and retrieval of members.
 */
public interface MemberService {

    /**
     * Registers a new member in the system.
     * <p>
     * This method handles business validations such as checking for duplicate entries
     * and setting default member properties.
     *
     * @param member the {@link Member} to register
     * @return the registered {@link Member} with any generated or default fields set
     */
    Member registerMember(Member member);

    /**
     * Updates the information of an existing member.
     *
     * @param member the {@link Member} containing updated data
     * @return the updated {@link Member}
     */
    Member updateMemberInfo(Member member);

    /**
     * Retrieves a list of all registered members.
     *
     * @return a {@link List} of all {@link Member} entities
     */
    List<Member> getAllMembers();
    Optional<Member> getMemberById(int id);
    List<Member> getAllActiveMembers();
}



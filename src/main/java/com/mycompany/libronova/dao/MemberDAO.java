/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.dao;

import com.mycompany.libronova.model.Member;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Coder
 */
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for managing {@link Member} entities.
 * <p>
 * This interface defines the contract for CRUD operations related to members (library users)
 * within the LibroNova system.
 */
public interface MemberDAO {

    /**
     * Persists a new member into the database.
     *
     * @param member the {@link Member} to be created
     * @return the created {@link Member} with any generated fields (e.g., ID)
     */
    Member create(Member member);

    /**
     * Retrieves a member by its unique ID.
     *
     * @param id the ID of the member to retrieve
     * @return an {@link Optional} containing the found {@link Member}, or empty if not found
     */
    Optional<Member> findById(int id);

    /**
     * Retrieves all members from the database.
     *
     * @return a {@link List} of all {@link Member} entities
     */
    List<Member> findAll();

    /**
     * Updates an existing member in the database.
     *
     * @param member the {@link Member} object containing updated information
     * @return the updated {@link Member}
     */
    Member update(Member member);

    /**
     * Deletes a member by its ID.
     *
     * @param id the ID of the member to delete
     */
    void delete(int id);
}


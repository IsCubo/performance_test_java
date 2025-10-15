/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service.impl;

import com.mycompany.libronova.dao.MemberDAO;
import com.mycompany.libronova.exception.ErrorCode;
import com.mycompany.libronova.exception.LibroNovaException;
import com.mycompany.libronova.exception.MemberDuplicateException;
import com.mycompany.libronova.model.Member;
import com.mycompany.libronova.model.User;
import com.mycompany.libronova.model.enums.MemberStatus;
import com.mycompany.libronova.service.MemberService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author Coder
 */
/**
 * Implementation of {@link MemberService} for member management operations.
 * <p>
 * Handles business logic for member registration, updates, and retrieval,
 * including validation rules and default property assignments.
 *
 * @author Coder
 */
public class MemberServiceImpl implements MemberService {

    private final MemberDAO memberDAO;

    /**
     * Constructs a new MemberServiceImpl with the specified DAO.
     *
     * @param memberDAO the data access object for member operations
     */
    public MemberServiceImpl(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    @Override
    public Member registerMember(Member member) {
        // Business Rule: Validate that member ID or email is unique
        // Assuming Member has a unique identifier (e.g., memberId or email)
        memberDAO.findById(member.getId()).ifPresent(m -> {
            throw new MemberDuplicateException();
        });

        // Set server-side properties
        member.setStatus(MemberStatus.ACTIVE);
        member.setCreatedAt(LocalDateTime.now());

        return memberDAO.create(member);
    }

    @Override
    public Member updateMemberInfo(Member member) {
        // Ensure the member exists before updating
        memberDAO.findById(member.getId())
                .orElseThrow(() -> new LibroNovaException(ErrorCode.INVALID_DATA));

        return memberDAO.update(member);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    @Override
    public Optional<Member> getMemberById(int id) {
        return memberDAO.findById(id);
    }

    @Override
    public List<Member> getAllActiveMembers() {
        // Filtramos para obtener solo socios activos.
        return memberDAO.findAll().stream()
                .filter(member -> member.getStatus() == MemberStatus.ACTIVE)
                .collect(Collectors.toList());
    }
}

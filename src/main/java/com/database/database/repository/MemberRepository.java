package com.database.database.repository;

import com.database.database.domain.Member;

public interface MemberRepository {
    Member save(Member member);
    Member findById(String memberId);
    void updateById(String memberId, int money);
    void deleteMemberById(String memberId);
}

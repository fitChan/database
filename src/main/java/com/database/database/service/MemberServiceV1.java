package com.database.database.service;

import com.database.database.domain.Member;
import com.database.database.repository.MemberRepositoryV0;
import com.database.database.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberRepositoryV1 memberRepository;


    public void sendMoney(String fromId, String toId, int money) throws SQLException {
        Member sendMember = memberRepository.findById(fromId);
        Member receiveMember = memberRepository.findById(toId);

        memberRepository.updateById(sendMember.getMemberId(), sendMember.getMoney()-money);
        validation(receiveMember);
        memberRepository.updateById(receiveMember.getMemberId(), receiveMember.getMoney()+money);

    }

    private void validation(Member sendMember) {
        if(sendMember.getMemberId().equals("ex")){
            throw new IllegalArgumentException("이체중 예외 발생");
        }
    }
}

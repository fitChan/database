package com.database.database.service;

import com.database.database.domain.Member;
import com.database.database.repository.MemberRepository;
import com.database.database.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Slf4j
public class MemberServiceV4{

    private final MemberRepository memberRepository;

    public MemberServiceV4(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void sendMoneyWithTransacTemplate(String fromId, String toId, int money){
        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money){
        Member sendMember = memberRepository.findById(fromId);
        Member receiveMember = memberRepository.findById(toId);

        memberRepository.updateById(sendMember.getMemberId(), sendMember.getMoney() - money);
        validation(receiveMember);
        memberRepository.updateById(receiveMember.getMemberId(), receiveMember.getMoney() + money);
    }

    private void validation(Member sendMember) {
        if (sendMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}

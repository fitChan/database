package com.database.database.service;

import com.database.database.domain.Member;
import com.database.database.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

@Slf4j
public class MemberServiceV3_2 {

    private final MemberRepositoryV3 memberRepository;
    private final TransactionTemplate transactionTemplate;

    public MemberServiceV3_2(MemberRepositoryV3 memberRepository, PlatformTransactionManager transactionManager) {
        this.memberRepository = memberRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void sendMoneyWithTransacTemplate(String fromId, String toId, int money){

        transactionTemplate.executeWithoutResult(s -> {
            try {
                bizLogic(fromId, toId, money);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
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

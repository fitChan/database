package com.database.database.service;

import com.database.database.domain.Member;
import com.database.database.repository.MemberRepositoryV2;
import com.database.database.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

    private final MemberRepositoryV3 memberRepository;
    private final PlatformTransactionManager transactionManager;


    public void sendMoneyWithTransactionManager(String fromId, String toId, int money) throws SQLException {
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            bizLogic(fromId, toId, money);
            transactionManager.commit(transaction);
        } catch (IllegalStateException e) {
            transactionManager.rollback(transaction);
            throw new IllegalStateException(e);
        } //finally 가 필요하지 않은 이유 : manager 에서 commit or rollback 이 일어난 후 알아서 닫힌다.


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

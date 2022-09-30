package com.database.database.service;

import com.database.database.domain.Member;
import com.database.database.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

    private final MemberRepositoryV2 memberRepository;
    private final DataSource dataSource;


    public void sendMoneyWithTransaction(String fromId, String toId, int money) throws SQLException {
        Connection connection = dataSource.getConnection();

        try {
            connection.setAutoCommit(false);
            bizLogic(connection, fromId, toId, money);
            connection.commit();
        } catch (IllegalStateException e) {
            connection.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(connection);
        }


    }

    private void bizLogic(Connection connection, String fromId, String toId, int money) throws SQLException {
        Member sendMember = memberRepository.findById(connection, fromId);
        Member receiveMember = memberRepository.findById(connection, toId);
        memberRepository.updateById(sendMember.getMemberId(), sendMember.getMoney() - money);
        validation(receiveMember);
        memberRepository.updateById(receiveMember.getMemberId(), receiveMember.getMoney() + money);
    }


    private void release(Connection connection) {
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (Exception e) {
                log.error("error", e);
            }
        }
    }

    private void validation(Member sendMember) {
        if (sendMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}

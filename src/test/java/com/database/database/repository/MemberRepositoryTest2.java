package com.database.database.repository;

import com.database.database.domain.Member;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import static com.database.database.connection.ConnectionConst.*;
@Slf4j
class MemberRepositoryTest2 {
    MemberRepositoryV1 repositorySample;

    @BeforeEach
    void beforeEach() throws Exception {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        repositorySample = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {

        Member member = new Member("cksdntjd32", 100000);
        repositorySample.save(member);

        Member findMember = repositorySample.findById(member.getMemberId());
        log.info("findMember = {}", findMember);

        Assertions.assertThat(findMember).isEqualTo(member);


        repositorySample.updateById(member.getMemberId(), 0);
        Member updatedMember = repositorySample.findById(member.getMemberId());
        Assertions.assertThat(updatedMember.getMoney()).isEqualTo(0);

        repositorySample.deleteMemberById(member.getMemberId());

        Assertions.assertThatThrownBy(()-> repositorySample.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);

    }

}
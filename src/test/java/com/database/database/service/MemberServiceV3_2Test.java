package com.database.database.service;

import com.database.database.domain.Member;
import com.database.database.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;

import static com.database.database.connection.ConnectionConst.*;

@Slf4j
class MemberServiceV3_2Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV3 memberRepository;
    private MemberServiceV3_2 memberService;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV3(dataSource);

        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

        memberService = new MemberServiceV3_2(memberRepository, transactionManager);
    }

    @AfterEach
    void after() throws SQLException {
        memberRepository.deleteMemberById(MEMBER_A);
        memberRepository.deleteMemberById(MEMBER_B);
        memberRepository.deleteMemberById(MEMBER_EX);
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException {
        //given

        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //WHEN
        memberService.sendMoneyWithTransacTemplate(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //then
        Member ma = memberRepository.findById(memberA.getMemberId());
        Member mb = memberRepository.findById(memberB.getMemberId());

        Assertions.assertThat(ma.getMoney()).isEqualTo(8000);
        Assertions.assertThat(mb.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체 예외 발생 & 트랜잭션은 적용")
    void accountTransferExceptionWithTransaction() throws SQLException {
        //given

        Member memberA = new Member(MEMBER_A, 10000);
        Member memberex = new Member(MEMBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberex);

        //WHEN

        Assertions.assertThatThrownBy(() ->
                memberService.sendMoneyWithTransacTemplate(
                        memberA.getMemberId(),
                        memberex.getMemberId(),
                        2000))
                .isInstanceOf(IllegalStateException.class);

        //then
        Member ma = memberRepository.findById(memberA.getMemberId());
        Member mex = memberRepository.findById(memberex.getMemberId());

        Assertions.assertThat(ma.getMoney()).isEqualTo(10000);
        Assertions.assertThat(mex.getMoney()).isEqualTo(10000);


    }
}
package com.database.database.service;

import com.database.database.domain.Member;
import com.database.database.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@SpringBootTest
class MemberServiceV3_4Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    @Autowired
    private MemberRepositoryV3 memberRepository;
    @Autowired
    private MemberServiceV3_3 memberService;

    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;

        public TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        MemberRepositoryV3 memberRepositoryV3() {
            return new MemberRepositoryV3(dataSource);
        }

        @Bean
        MemberServiceV3_3 memberServiceV3_3() {
            return new MemberServiceV3_3(memberRepositoryV3());
        }
    }

    /*@BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV3(dataSource);
        memberService = new MemberServiceV3_3(memberRepository);
    }*/

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
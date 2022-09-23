package com.database.database.repository;

import com.database.database.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Slf4j
class MemberRepositoryTest {
    MemberRepository memberRepository = new MemberRepository();

    @Test
    void crud() throws SQLException {

        Member member = new Member("cksdntjd32", 100000);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getMemberId());
        log.info("findMember = {}", findMember);

        Assertions.assertThat(findMember).isEqualTo(member);


        memberRepository.updateById(member.getMemberId(), 0);
        Member updatedMember = memberRepository.findById(member.getMemberId());
        Assertions.assertThat(updatedMember.getMoney()).isEqualTo(0);

        memberRepository.deleteMemberById(member.getMemberId());

        Assertions.assertThatThrownBy(()-> memberRepository.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);

    }

}
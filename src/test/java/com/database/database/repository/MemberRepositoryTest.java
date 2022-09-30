package com.database.database.repository;

import com.database.database.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Slf4j
class MemberRepositoryTest {
    MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {

        Member member = new Member("cksdntjd32", 100000);
        memberRepositoryV0.save(member);

        Member findMember = memberRepositoryV0.findById(member.getMemberId());
        log.info("findMember = {}", findMember);

        Assertions.assertThat(findMember).isEqualTo(member);


        memberRepositoryV0.updateById(member.getMemberId(), 0);
        Member updatedMember = memberRepositoryV0.findById(member.getMemberId());
        Assertions.assertThat(updatedMember.getMoney()).isEqualTo(0);

        memberRepositoryV0.deleteMemberById(member.getMemberId());

        Assertions.assertThatThrownBy(()-> memberRepositoryV0.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);

    }

}
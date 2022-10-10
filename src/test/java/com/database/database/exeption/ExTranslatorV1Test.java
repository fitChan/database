package com.database.database.exeption;


import com.database.database.connection.ConnectionConst;
import com.database.database.domain.Member;
import com.database.database.repository.exception.DatabaseException;
import com.database.database.repository.exception.DuplicatedKeyException;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static com.database.database.connection.ConnectionConst.*;

@Slf4j
public class ExTranslatorV1Test {


    Repository repository;
    Service service;

    @BeforeEach
    void init(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        repository = new Repository(dataSource);
        service = new Service(repository);
    }

    @Test
    void duplicateKeySave(){
        service.create("memA");
        service.create("memA");
    }

    @RequiredArgsConstructor
    static class Service {
        private final Repository repository;

        public void create(String memberId) {
            try {
                repository.save(new Member(memberId, 0));
                log.info("saveId = {}", memberId);
            } catch (DuplicatedKeyException e) {
                log.info("키 중복, 복구 시도 ");
                String s = generateNewId(memberId);
                log.info("retryId = {}", s);
                repository.save(new Member(s, 0));
            }catch (DatabaseException e){
                log.info("데이터 접근 계층 예외", e);
                throw e;
            }
        }

        private String generateNewId(String memberId) {
            return memberId + new Random().nextInt(10000);
        }
    }


    @RequiredArgsConstructor
    static class Repository {
        private final DataSource dataSource;

        public Member save(Member member) {
            String sql = "insert into member(member_id, money) values(?,?)";
            Connection con = null;
            PreparedStatement pst = null;
            try {
                con = dataSource.getConnection();
                pst = con.prepareStatement(sql);
                pst.setString(1, member.getMemberId());
                pst.setInt(2, member.getMoney());
                pst.executeUpdate();
                return member;
            } catch (SQLException e) {
                if (e.getErrorCode() == 23505) {
                    throw new DuplicatedKeyException(e);
                }
                throw  new DatabaseException(e);
            } finally {
                JdbcUtils.closeStatement(pst);
                JdbcUtils.closeConnection(con);
            }
        }
    }
}

package com.database.database.repository;

import com.database.database.connection.DBConnectionUtil;
import com.database.database.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class MemberRepository {
    public Member save(Member member) throws SQLException {

        String sql = "insert into member(member_id, money) values (?,?)";

        Connection connection = null;
        PreparedStatement statement = null;


        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, member.getMemberId());
            statement.setInt(2, member.getMoney());
            statement.executeUpdate();  /*retrun값이 int형인것을 확인할수 있는데 이는 해당 update가 db 에 몇개의 row에 영향을
                                                주었는지를 반환해준다. 만약 10개의 row에 영향을 주었다면 10이 반환된다. */
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(connection, statement, null);
        }
    }

    private void close(Connection connection, Statement statement, ResultSet set) {

        if(set != null){
            try {
                set.close();
            } catch (SQLException e) {
                log.error("error", e);
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("error", e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("error", e);
            }
        }

    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

}
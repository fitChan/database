package com.database.database.repository;

import com.database.database.domain.Member;
import com.database.database.repository.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV4_1 implements MemberRepository {

    private final DataSource dataSource;

    public MemberRepositoryV4_1(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Member save(Member member) {

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
            throw new DatabaseException(e);
        } finally {
            close(connection, statement, null);
        }
    }

    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;


        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, memberId);
            set = statement.executeQuery();
            if (set.next()) { //next를 해줘야 실제 데이터를 가져올 수 있음
                Member member = new Member();

                member.setMemberId(set.getString("member_id"));
                member.setMoney(set.getInt("money"));

                return member;
            } else {
                throw new NoSuchElementException("member not found memberId = " + memberId);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            close(connection, statement, null);
        }

    }

    @Override
    public void updateById(String memberId, int money) {

        String sql = "update member set money = ? where member_id=?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, money);
            statement.setString(2, memberId);
            int i = statement.executeUpdate();

            log.info("resultSize = {}", i);

        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            close(connection, statement, null);
        }

    }

    @Override
    public void deleteMemberById(String memberId) {
        String sql = "delete from member where member_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, memberId);
            int set1 = statement.executeUpdate();

            log.info("Result set = {}", set1);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            close(connection, statement, null);
        }
    }


    private Connection getConnection() throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        log.info("getConnection={}, class={}", connection, connection.getClass());
        return connection;
    }

    private void close(Connection connection, Statement statement, ResultSet set) {
        JdbcUtils.closeResultSet(set);
        JdbcUtils.closeStatement(statement);
        DataSourceUtils.releaseConnection(connection, dataSource);
    }
}

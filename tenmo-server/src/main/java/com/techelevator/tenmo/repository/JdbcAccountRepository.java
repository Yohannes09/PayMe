package com.techelevator.tenmo.repository;

import java.util.Optional;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcAccountRepository implements AccountRepository {

    private final JdbcTemplate jdbcTemplate;
    private final static String SELECT_SQL = "SELECT * FROM account act ";

    public JdbcAccountRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcAccountRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcAccountRepository(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Account> getByAccountId(int acountId) {
        String sql = SELECT_SQL + "WHERE act.account_id = ?";

        try{
            SqlRowSet row = jdbcTemplate.queryForRowSet(sql, acountId);
            if(row.next())
                return Optional.ofNullable(mapRowToAccount(row));

        }catch (CannotGetJdbcConnectionException e){
            // log eventually
        }
        return Optional.empty();
    }

    @Override
    public void withdraw(int accountId, double balance) {
        String sql = "UPDATE account " +
                "SET balance = balance - ? " +
                "WHERE account_id = ?";

        try{
            jdbcTemplate.update(sql, balance, accountId);

        }catch (CannotGetJdbcConnectionException e){

        }catch (DataIntegrityViolationException e){

        }
    }

    @Override
    public void deposit(int accountId, double balance) {
        String sql = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE account_id = ?";

        try{
            jdbcTemplate.update(sql, balance, accountId);

        }catch (CannotGetJdbcConnectionException e){

        }catch (DataIntegrityViolationException e){

        }

    }


    @Override
    public int deleteById(int accountId) {
        String deleteUser = "DELETE FROM tenmo_user WHERE user_id = (SELECT user_id FROM account WHERE account_id = ?)";
        String deleteAccount = "DELETE FROM account WHERE account_id = ?";
        try{
            jdbcTemplate.update(deleteAccount, accountId);
            return jdbcTemplate.update(deleteUser);
        }catch (DataIntegrityViolationException e){

        }catch (CannotGetJdbcConnectionException e){

        }
        return 0;
    }


    @Override
    public boolean accountExists(int acountId) {
        String sql = SELECT_SQL + "WHERE act.account_id = ?";

        try{
            SqlRowSet row = jdbcTemplate.queryForRowSet(sql, acountId);
            if(row.next())
                return true;

        }catch (CannotGetJdbcConnectionException e){
            // log eventually
        }
        return false;
    }

    public Account mapRowToAccount(SqlRowSet row){
        return new Account(
                row.getInt("account_id"),
                row.getInt("user_id"),
                row.getDouble("balance"));
    }


}

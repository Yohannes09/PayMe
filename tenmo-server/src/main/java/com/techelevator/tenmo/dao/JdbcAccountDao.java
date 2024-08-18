package com.techelevator.tenmo.dao;

import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.zaxxer.hikari.util.DriverDataSource;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
    private final static String SELECT_SQL = "SELECT * FROM account act ";

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcAccountDao(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcAccountDao(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Account> findByAccountId(int acountId) {
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
    public Optional<Account> findByUsername(String username) {
        String sql = "SELECT * FROM account act " +
                "JOIN tenmo_user tu ON tu.user_id = act.user_id " +
                "WHERE LOWER(tu.username) = LOWER(?)";

        try{
            SqlRowSet row = jdbcTemplate.queryForRowSet(sql, username);

            if(row.next()) {
                return Optional.ofNullable(mapRowToAccount(row));
            }

        }catch (CannotGetJdbcConnectionException e){
            // log eventually
            throw new DaoException();
        }
        return Optional.empty();
    }

    @Override
    public boolean withdraw(int accountId, double balance) {
        double previousAccountBalance = findByAccountId(accountId).get().getBalance();
        String sql = "UPDATE account " +
                "SET balance = balance - ? " +
                "WHERE account_id = ?";

        try{
            jdbcTemplate.update(sql, balance, accountId);
            if(findByAccountId(accountId).get().getBalance() != previousAccountBalance)
                return true;

        }catch (CannotGetJdbcConnectionException e){

        }catch (DataIntegrityViolationException e){

        }
        return false;
    }

    // look into changing this. this could be void or return how many rows were updated
    @Override
    public boolean deposit(int accountId, double balance) {
        double previousAccountBalance = findByAccountId(accountId).get().getBalance();
        String sql = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE account_id = ?";

        try{
            jdbcTemplate.update(sql, balance, accountId);
            if(findByAccountId(accountId).get().getBalance() != previousAccountBalance)
                return true;

        }catch (CannotGetJdbcConnectionException e){

        }catch (DataIntegrityViolationException e){

        }
        return false;
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
    public double getAccountBalance(int id) {
        return findByAccountId(id).get().getBalance();
    }

    public Account mapRowToAccount(SqlRowSet row){
        return new Account(
                row.getInt("account_id"),
                row.getInt("user_id"),
                row.getDouble("balance"));
    }

}

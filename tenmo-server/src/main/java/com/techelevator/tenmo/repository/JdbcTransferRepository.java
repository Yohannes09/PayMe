package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTransferRepository implements TransferRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public JdbcTransferRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Optional<Transfer> proccessTransfer(int senderId,
                                               int recipientId,
                                               int transferStatusId,
                                               int transfterTypeId,
                                               double amount) {

        String sql = "INSERT INTO transfer(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES(?, ?, ?, ?, ?) RETURNING transfer_id;";

        try{
            int newTransferId = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    transfterTypeId,
                    transferStatusId,
                    senderId,
                    recipientId,
                    amount
            );

            return getTransferById(newTransferId);

        }catch (DataIntegrityViolationException e){

        }catch (CannotGetJdbcConnectionException e){

        }
        return Optional.empty();
    }

    @Override
    public List<Transfer> accountTransferHistory(int accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer tr " +
                "JOIN account ac ON ac.account_id = tr.account_from " +
                "OR ac.account_id = tr.account_to " +
                "WHERE ac.account_id = ?; ";

        SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(sql, accountId);

        while(sqlRow.next())
            transfers.add(mapTransferToRow(sqlRow));

        return transfers;
    }

    @Override
    public Optional<Transfer> getTransferById(int id) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";

        try{
            SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(sql, id);

            if(sqlRow.next())
                return Optional.ofNullable(mapTransferToRow(sqlRow));

        }catch (CannotGetJdbcConnectionException e){

        }
        return Optional.empty();
    }

    @Override
    public List<Transfer> getTransfers() {
        String sql = "SELECT * FROM transfer;";
        List<Transfer> transfers = new ArrayList<>();

        SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(sql);

        while(sqlRow.next())
            transfers.add(mapTransferToRow(sqlRow));

        return transfers;
    }

    @Override
    public int deleteTransfer(int transferId) {
        String sql = "DELETE FROM transfer WHERE transfer_id = ?; ";

        try{
            int rowsDeleted = jdbcTemplate.update(
                    sql,
                    int.class,
                    transferId
            );

            return rowsDeleted;

        }catch (DataIntegrityViolationException e){

        }catch (CannotGetJdbcConnectionException e){

        }
        return 0;
    }

    @Override
    public List<Transfer> getAccountTransferStatus(int accountId, int transferStatusId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer tr " +
                "WHERE (account_from = ? " +
                "OR account_to = ?) " +
                "AND transfer_status_id = ? ; ";
        try{
            SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(sql, accountId, accountId, transferStatusId);

            while(sqlRow.next())
                transfers.add(mapTransferToRow(sqlRow));

        }catch (CannotGetJdbcConnectionException e){

        }
        return transfers;
    }


    public Transfer mapTransferToRow(SqlRowSet sqlRow){
        return new Transfer(
                sqlRow.getInt("transfer_id"),
                sqlRow.getInt("transfer_type_id"),
                sqlRow.getInt("transfer_status_id"),
                sqlRow.getInt("account_from"),
                sqlRow.getInt("account_to"),
                sqlRow.getDouble("amount")
        );
    }

}

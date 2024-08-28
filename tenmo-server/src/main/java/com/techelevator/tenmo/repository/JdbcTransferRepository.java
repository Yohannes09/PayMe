package com.techelevator.tenmo.repository;

import com.techelevator.tenmo.dto.TransferResponseDto;
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

    @Override
    public List<TransferResponseDto> getTransferHistoryTEST(int accountId) {
        List<TransferResponseDto> transfers = new ArrayList<>();
        String sql = "SELECT tr.transfer_id, sender.username AS sender_username, recipient.username AS recipient_username, " +
                "sender_ac.account_id AS sender_id, recipient_ac.account_id AS recipient_id,  tr.amount " +
                "FROM transfer tr " +
                "JOIN account sender_ac ON sender_ac.account_id = tr.account_from " +
                "JOIN tenmo_user sender ON sender.user_id = sender_ac.user_id " +
                "JOIN account recipient_ac ON recipient_ac.account_id = tr.account_to " +
                "JOIN tenmo_user recipient ON recipient.user_id = recipient_ac.user_id " +
                "WHERE tr.account_from = ? OR tr.account_to = ?";

        try{
            SqlRowSet sqlRows = jdbcTemplate.queryForRowSet(sql, accountId, accountId);

            while(sqlRows.next())
                transfers.add(mapTransferResponseToRow(sqlRows));

            return transfers;

        }catch (CannotGetJdbcConnectionException connectionException){

        }

        return List.of();
    }


    public TransferResponseDto mapTransferResponseToRow(SqlRowSet sqlRow){
        return new TransferResponseDto(
                sqlRow.getInt("transfer_id"),
                sqlRow.getInt("sender_id"),
                sqlRow.getInt("recipient_id"),
                sqlRow.getString("sender_username"),
                sqlRow.getString("recipient_username"),
                sqlRow.getDouble("amount")
        );
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

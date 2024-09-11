package com.techelevator.tenmo.repository.notused;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTransferRepository implements TransferRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferRepository.class);

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
    public Optional<Transfer> processTransfer(
            int transferTypeId, int transferStatusId, int senderAccountId, int recipientAccountId, double amount) {

        String sql = "INSERT INTO transfer(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES(?, ?, ?, ?, ?) RETURNING transfer_id; ";

        try{
            // Integer allows to check for null
            Integer newTransferId = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    transferTypeId,
                    transferStatusId,
                    senderAccountId,
                    recipientAccountId,
                    amount
            );

            return newTransferId != null ? getTransferById(newTransferId) : Optional.empty();
        }catch(DataIntegrityViolationException dataException){
            LOGGER.error("DataIntegrityViolation, ", dataException.getMessage());
        }

        return Optional.empty();
    }


    @Override
    public Optional<Transfer> getTransferById(int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";

        try{
            SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(sql, transferId);

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
    public int deleteTransferById(int transferId) {
        String sql = "DELETE FROM transfer WHERE transfer_id = ?; ";

        try{
            int rowsDeleted = jdbcTemplate.update(
                    sql,
                    int.class,
                    transferId
            );

            return rowsDeleted;

        }catch (DataIntegrityViolationException e){
            LOGGER.error("DataIntegrityViolation " , e.getMessage());
        }catch (CannotGetJdbcConnectionException e){
            LOGGER.error("JdbcConnection " , e.getMessage());
        }
        return 0;
    }

    @Override
    public List<TransferResponseDto> getTransferHistory(int accountId) {
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
            LOGGER.error("CannotGetJdbcConnection: ", connectionException.getMessage());
        }

        return List.of();
    }

    @Override
    public Optional<Transfer> updateTransferStatus(int transferId, int newTransferStatusId) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?";

        try {
            Optional<Transfer> transfer = getTransferById(transferId);

            if(!transfer.isPresent())
                return Optional.empty();

            jdbcTemplate.update(sql, newTransferStatusId, transferId);
            return getTransferById(transferId);
        }catch (CannotGetJdbcConnectionException connectionException){

        }
        return Optional.empty();
    }

    @Override
    public List<TransferResponseDto> accountTransferStatus(int transferStatusId, int accountId) {
        List<TransferResponseDto> transfers = new ArrayList<>();
        String sql = "SELECT tr.transfer_id, sender.username AS sender_username, recipient.username AS recipient_username, " +
                "sender_ac.account_id AS sender_id, recipient_ac.account_id AS recipient_id,  tr.amount " +
                "FROM transfer tr " +
                "JOIN account sender_ac ON sender_ac.account_id = tr.account_from " +
                "JOIN tenmo_user sender ON sender.user_id = sender_ac.user_id " +
                "JOIN account recipient_ac ON recipient_ac.account_id = tr.account_to " +
                "JOIN tenmo_user recipient ON recipient.user_id = recipient_ac.user_id " +
                "WHERE tr.transfer_type_id = 1 " +
                "AND tr.transfer_status_id = ? " +
                "AND tr.account_to = ? ;";
        try {
            SqlRowSet sqlRows = jdbcTemplate.queryForRowSet(sql, transferStatusId, accountId);

            while(sqlRows.next())
                transfers.add(mapTransferResponseToRow(sqlRows));

            return transfers;
        }catch (CannotGetJdbcConnectionException connectionException){

        }

        return List.of();
    }


    private TransferResponseDto mapTransferResponseToRow(SqlRowSet sqlRow){
        return new TransferResponseDto(
                sqlRow.getLong("transfer_id"),
                sqlRow.getLong("sender_id"),
                sqlRow.getLong("recipient_id"),
                sqlRow.getString("sender_username"),
                sqlRow.getString("recipient_username"),
                sqlRow.getBigDecimal("amount")
        );
    }

    private Transfer mapTransferToRow(SqlRowSet sqlRow){
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
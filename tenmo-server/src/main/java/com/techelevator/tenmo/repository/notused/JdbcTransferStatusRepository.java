package com.techelevator.tenmo.repository.notused;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JdbcTransferStatusRepository implements TransferStatusRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferStatusRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTransferStatusRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        this.jdbcTemplate = new JdbcTemplate();
    }

    @Override
    public Optional<TransferStatus> createTransferStatus(String description) {
        String sql = "INSERT INTO transfer_type VALUES(?) RETURNING transfer_status_id; ";
        try{
            int newTransferStatusId = jdbcTemplate.queryForObject(
                    sql,
                    int.class,
                    description
            );

            return findTransferStatusById(newTransferStatusId);
        }catch (DataIntegrityViolationException e){

        }
        return Optional.empty();
    }

    @Override
    public Optional<TransferStatus> findTransferStatusById(int id) {
        String sql = "SELECT * FROM transfer_status WHERE transfer_status_id = ?; ";
        try{
            SqlRowSet sqlRows = jdbcTemplate.queryForRowSet(sql, id);
            if(sqlRows.next())
                return Optional.ofNullable(mapStausToRow(sqlRows));

        }catch (CannotGetJdbcConnectionException e){

        }
        return Optional.empty();
    }

    @Override
    public Optional<List<TransferStatus>> getAllTransferStatus() {
        return Optional.empty();
    }

    @Override
    public int deleteTransferStatus(int id) {
        return 0;
    }

    public TransferStatus mapStausToRow(SqlRowSet sqlRow){
        return new TransferStatus(
                sqlRow.getInt("transfer_status_id"),
                sqlRow.getString("transfer_status_desc")
        );
    }
}

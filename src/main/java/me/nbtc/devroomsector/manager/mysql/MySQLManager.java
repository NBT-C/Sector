package me.nbtc.devroomsector.manager.mysql;

import me.cobeine.sqlava.connection.auth.BasicMySQLCredentials;
import me.cobeine.sqlava.connection.auth.CredentialsRecord;
import me.cobeine.sqlava.connection.database.MySQLConnection;
import me.cobeine.sqlava.connection.database.table.Table;
import me.nbtc.devroomsector.Sector;
import org.bukkit.configuration.Configuration;

import java.sql.SQLException;

public class MySQLManager {
    private final Configuration data = Sector.getInstance().getSettings().getConfig();
    private final MySQLConnection connection = new MySQLConnection(CredentialsRecord.builder()
            .add(BasicMySQLCredentials.DATABASE, data.get("MySQL.Database"))
            .add(BasicMySQLCredentials.HOST, data.get("MySQL.Host"))
            .add(BasicMySQLCredentials.PASSWORD, data.get("MySQL.Password"))
            .add(BasicMySQLCredentials.USERNAME, data.get("MySQL.Username"))
            .add(BasicMySQLCredentials.JDBC_URL, "jdbc:mysql://" + data.get("MySQL.Host") + "/" + data.get("MySQL.Database") + "?autoReconnect=true")
            .add(BasicMySQLCredentials.POOL_SIZE, data.getInt("MySQL.PoolSize"))
            .build());


    public void connect() {
        connection.connect();
        checkTable();
    }

    public MySQLConnection getConnector() {
        return connection;
    }

    private void checkTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS regions (" +
                "ID VARCHAR(255) PRIMARY KEY NOT NULL," +
                "NAME VARCHAR(255)," +
                "CORNER_1 TEXT," +
                "CORNER_2 TEXT," +
                "WHITELIST TEXT," +
                "FLAGS TEXT" +
                ");";

        try {
            connection.prepareStatement(createTableSQL).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
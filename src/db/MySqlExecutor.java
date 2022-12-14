package db;

import utils.IDBConfigs;
import utils.PropertiesConfig;

import java.sql.*;
import java.util.Map;

public class MySqlExecutor implements IDBExecutor {

  private static Connection connection = null;
  private static Statement statement = null;

  private void createDbStatement() {
    try {
      if(connection == null || connection.isClosed()) {
        IDBConfigs configsReader = new PropertiesConfig();
        Map<String, String> config = configsReader.read();

        connection = DriverManager.getConnection(config.get("url") + String.format("/%s", config.get("db_name")) +
                "?verifyServerCertificate=false" +
                "&useSSL=false" +
                "&requireSSL=false" +
                "&useLegacyDatetimeCode=false" +
                "&serverTimezone=UTC"
                , config.get("username"), config.get("password"));
      }
      if(statement == null || statement.isClosed()) {
        statement = connection.createStatement();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() {
    try {
      if(statement != null && !statement.isClosed()) {
        statement.close();
        statement = null;
      }
      if(connection != null && !connection.isClosed()) {
        connection.close();
        connection = null;
      }
    } catch (SQLException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override
  public void execute(String request) {
    createDbStatement();

    try {
      statement.execute(request);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ResultSet executeQuery(String request) {
    createDbStatement();

    try {
      return statement.executeQuery(request);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}

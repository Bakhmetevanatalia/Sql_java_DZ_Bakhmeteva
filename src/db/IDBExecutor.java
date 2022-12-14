package db;

import java.sql.ResultSet;

public interface IDBExecutor {
  void execute(String request);
  ResultSet executeQuery(String request);
  void close();
}

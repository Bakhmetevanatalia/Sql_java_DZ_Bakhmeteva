package tables;

import db.IDBExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class AbsTable<T> {
    protected final String tableName;
    protected final IDBExecutor dbExecutor;

    public AbsTable(String tableName, IDBExecutor dbExecutor) {
        this.tableName = tableName;
        this.dbExecutor = dbExecutor;
    }

    public void create(List<String> columnNames) {
        dbExecutor.execute(String.format("create table if not exists `%s` (%s)", tableName, String.join(",", columnNames)));
    }

    public abstract void create();

    public abstract void insert(List<String> columnValues);

    public abstract T selectItemByColumn(String column, String value);

    public abstract List<T> selectAllByColumn(String column, String value);

    public int size() {
        try (ResultSet result = dbExecutor.executeQuery(String.format("select count(*) from `%s`", tableName))) {
            if (!result.next())
                throw new RuntimeException("Unexpected error");
            return result.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        dbExecutor.execute(String.format("drop table if exists `%s`", tableName));
    }

    public String getName() {
        return tableName;
    }
}

package tables;

import data.Curator;
import db.IDBExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CuratorTable extends AbsTable<Curator> {

    public CuratorTable(IDBExecutor dbExecutor) {
        super("curators", dbExecutor);
    }

    @Override
    public void create() {
        List<String> columns = Arrays.asList(
                "`id` int auto_increment",
                "`fio` varchar(50)",
                "primary key (`id`)"
        );
        create(columns);
    }

    @Override
    public void insert(List<String> columnValues) {
        String values = columnValues.stream().map(s -> "'"+ s + "'").collect(Collectors.joining(", "));
        String query = String.format("insert into `%s` (`fio`) values (%s)", tableName, values);
        dbExecutor.execute(query);
    }

    @Override
    public Curator selectItemByColumn(String column, String value) {
        String query = String.format("select * from `%s` where `%s` = '%s' limit 1", tableName, column, value);
        try (ResultSet result = dbExecutor.executeQuery(query)) {
            if (result.next()) {
                return new Curator(result.getInt(1), result.getString(2));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Curator> selectAllByColumn(String column, String value) {
        List<Curator> resultList = new ArrayList<>();
        String query = String.format("select * from `%s` where `%s` = '%s'", tableName, column, value);
        try (ResultSet result = dbExecutor.executeQuery(query)) {
            while (result.next()) {
                resultList.add(new Curator(result.getInt(1), result.getString(2)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
}

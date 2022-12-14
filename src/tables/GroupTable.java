package tables;

import data.Group;
import db.IDBExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GroupTable extends AbsTable<Group> {

    public GroupTable(IDBExecutor dbExecutor) {
        super("groups", dbExecutor);
    }

    @Override
    public void create() {
        List<String> columns = Arrays.asList(
                "`id` int auto_increment",
                "`name` varchar(50) NOT NULL",
                "`id_curator` int",
                "primary key (`id`)",
                "foreign key(`id_curator`) references `curators`(`id`)"
        );
        create(columns);
    }

    @Override
    public void insert(List<String> columnValues) {
        String values = columnValues.stream().map(s -> "'"+ s + "'").collect(Collectors.joining(", "));
        String query = String.format("insert into `%s` (`name`, `id_curator`) values (%s)", tableName, values);
        dbExecutor.execute(query);
    }

    @Override
    public Group selectItemByColumn(String column, String value) {
        String query = String.format("select * from `%s` where `%s` = '%s' limit 1", tableName, column, value);
        try (ResultSet result = dbExecutor.executeQuery(query)) {
            if (result.next()) {
                return new Group(result.getInt(1), result.getString(2), result.getInt(3));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Group> selectAllByColumn(String column, String value) {
        List<Group> resultList = new ArrayList<>();
        String query = String.format("select * from `%s` where `%s` = '%s'", tableName, column, value);
        try (ResultSet result = dbExecutor.executeQuery(query)) {
            while (result.next()) {
                resultList.add(new Group(result.getInt(1), result.getString(2), result.getInt(3)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
}

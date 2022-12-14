package tables;

import data.Gender;
import data.Student;
import db.IDBExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StudentTable extends AbsTable<Student> {

    public StudentTable(IDBExecutor idbExecutor) {
        super("students", idbExecutor);
    }

    @Override
    public void create() {
        List<String> columns = Arrays.asList(
                "`id` int auto_increment",
                "`fio` varchar(50)",
                "`sex` varchar(10)",
                "`id_group` int",
                "primary key (`id`)",
                "foreign key (`id_group`) references `groups`(`id`)"
        );
        create(columns);
    }

    @Override
    public void insert(List<String> columnValues) {
        String values = columnValues.stream().map(s -> "'"+ s + "'").collect(Collectors.joining(", "));
        String query = String.format("insert into `%s` (`fio`, `sex`, `id_group`) values (%s)", tableName, values);
        dbExecutor.execute(query);
    }

    @Override
    public Student selectItemByColumn(String column, String value) {
        String query = String.format("select * from `%s` where `%s` = '%s' limit 1", tableName, column, value);
        try (ResultSet result = dbExecutor.executeQuery(query)) {
            if (result.next()) {
                return new Student(result.getInt(1), result.getString(2),
                        Gender.fromString(result.getString(3)), result.getInt(4));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> selectAllByColumn(String column, String value) {
        List<Student> resultList = new ArrayList<>();
        String query = String.format("select * from `%s` where `%s` = '%s'", tableName, column, value);
        try (ResultSet result = dbExecutor.executeQuery(query)) {
            while (result.next()) {
                resultList.add(new Student(result.getInt(1), result.getString(2),
                        Gender.fromString(result.getString(3)), result.getInt(4)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
}

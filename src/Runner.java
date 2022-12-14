import data.Curator;
import data.Gender;
import data.Group;
import data.Student;
import db.IDBExecutor;
import db.MySqlExecutor;
import tables.AbsTable;
import tables.CuratorTable;
import tables.GroupTable;
import tables.StudentTable;
import ui.Menu;
import utils.Console;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Runner implements AutoCloseable {
    private final IDBExecutor dbExecutor;
    private final AbsTable<Curator> curatorTable;
    private final AbsTable<Group> groupTable;
    private final AbsTable<Student> studentTable;

    public static void main(String... args) {
        try (Runner app = new Runner()) {
            app.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runner() {
        dbExecutor = new MySqlExecutor();

        curatorTable = new CuratorTable(dbExecutor);
        groupTable = new GroupTable(dbExecutor);
        studentTable = new StudentTable(dbExecutor);

        checkTables();
    }

    private void checkTables() {
        List<String> tableNames = new ArrayList<>();

        try (ResultSet result = dbExecutor.executeQuery("show tables")) {
            while (result.next()) {
                tableNames.add(result.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!tableNames.contains(curatorTable.getName()))
            createTable(curatorTable);
        if (!tableNames.contains(groupTable.getName()))
            createTable(groupTable);
        if (!tableNames.contains(studentTable.getName()))
            createTable(studentTable);
    }

    private <T> void createTable(AbsTable<T> table) {
        table.create();
        System.out.printf("Таблица '%s' создана.\n", table.getName());
    }

    private void run() {
        rawInsertData();
        Menu.create("Выберите действие")
                .addAction(Menu.createSubMenu("Добавить данные")
                        .addAction("Добавить куратора", this::addCurator)
                        .addAction("Добавить группу", this::addGroup)
                        .addAction("Добавить студента", this::addStudent))
                .addAction("Вывести информацию о всех студентах", this::showStudentsInfo)
                .addAction("Вывести количество студентов", this::showStudentsCount)
                .addAction("Вывести студенток", this::showGirls)
                .addAction("Вывести список групп с их кураторами", this::showGroups)
                .addAction("Вывести студентов определенной группы", this::showGroup)
                .addAction("Сменить куратора группы", this::changeGroupCurator)
                .execute();
    }

    private void rawInsertData() {
        // 4 куратора
        curatorTable.insert(Arrays.asList("Кураторский Иван Семенович"));
        curatorTable.insert(Arrays.asList("Степной Иван Алексеевич"));
        curatorTable.insert(Arrays.asList("Великий Ярослав Олегович"));
        curatorTable.insert(Arrays.asList("Четвертый Сергей Васильевич"));
        // 3 группы
        groupTable.insert(Arrays.asList("Группа11", Integer.toString(1)));
        groupTable.insert(Arrays.asList("Группа22", Integer.toString(2)));
        groupTable.insert(Arrays.asList("Группа33", Integer.toString(3)));
        // 15 студентов
        studentTable.insert(Arrays.asList("Иванов Иван Иванович", Gender.MALE.toString(), Integer.toString(1)));
        studentTable.insert(Arrays.asList("Сидоров Сидор Сидорович", Gender.MALE.toString(), Integer.toString(1)));
        studentTable.insert(Arrays.asList("Веселая Ольга Вячеславовна", Gender.FEMALE.toString(), Integer.toString(2)));
        studentTable.insert(Arrays.asList("Тимофеев Тимофей Александрович", Gender.MALE.toString(), Integer.toString(2)));
        studentTable.insert(Arrays.asList("Странный Игорь Иванович", Gender.MALE.toString(), Integer.toString(3)));
        studentTable.insert(Arrays.asList("Странная Инна Игоревна", Gender.FEMALE.toString(), Integer.toString(3)));
        studentTable.insert(Arrays.asList("Тихонов Василий Эдуардович", Gender.MALE.toString(), Integer.toString(1)));
        studentTable.insert(Arrays.asList("Стебновская Лариса Николаевна", Gender.FEMALE.toString(), Integer.toString(1)));
        studentTable.insert(Arrays.asList("Тюрина Елизавета Игоревна", Gender.FEMALE.toString(), Integer.toString(3)));
        studentTable.insert(Arrays.asList("Желнов Николай Николаевич", Gender.MALE.toString(), Integer.toString(1)));
        studentTable.insert(Arrays.asList("Сидорова Степанида Степановна", Gender.FEMALE.toString(), Integer.toString(2)));
        studentTable.insert(Arrays.asList("Моренков Евгений Иванович", Gender.MALE.toString(), Integer.toString(2)));
        studentTable.insert(Arrays.asList("Любимов Иван Иванович", Gender.MALE.toString(), Integer.toString(3)));
        studentTable.insert(Arrays.asList("Задорнов Иван Иванович", Gender.MALE.toString(), Integer.toString(1)));
        studentTable.insert(Arrays.asList("Иванченко Любовь Ивановна", Gender.FEMALE.toString(), Integer.toString(1)));
    }

    private void addCurator() {
        String fullName = Console.getString("ФИО куратора");
        curatorTable.insert(Arrays.asList(fullName));
        System.out.println("Куратор добавлен.");
    }

    private void addGroup() {
        Curator curator = getCurator();
        if (curator == null)
            return;

        String name = Console.getString("Имя группы");
        groupTable.insert(Arrays.asList(name, Integer.toString(curator.getId())));
        System.out.println("Группа добавлена.");
    }

    private void addStudent() {
        Group group = getGroup();
        if (group == null)
            return;

        String fullName = Console.getString("ФИО студента");
        Gender gender = getGender();
        studentTable.insert(Arrays.asList(fullName, gender.toString(), Integer.toString(group.getId())));
        System.out.println("Студент добавлен.");
    }

    private void showStudentsInfo() {
        String separator = "+" + String.join("", Collections.nCopies(133, "-")) + "+";

        System.out.println(separator);
        System.out.printf("| %-4s | %-50s | %-7s | %-8s | %-50s |\n", "ID", "ФИО", "Пол", "Группа", "Куратор");
        System.out.println(separator);

        String query = String.format(
                "select s.id, s.fio, s.sex, g.name, c.fio " +
                        "from `%s` as s " +
                        "join `%s` as g on g.id = s.id_group " +
                        "join `%s` as c on c.id = g.id_curator",
                studentTable.getName(), groupTable.getName(), curatorTable.getName());
        try (ResultSet result = dbExecutor.executeQuery(query)) {
            while (result.next()) {
                System.out.printf("| %-4d ", result.getInt(1));
                System.out.printf("| %-50s ", result.getString(2));
                System.out.printf("| %-7s ", result.getString(3));
                System.out.printf("| %-8s ", result.getString(4));
                System.out.printf("| %-50s |\n", result.getString(5));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(separator);
    }

    private void showStudentsCount() {
        System.out.println(studentTable.size());
    }

    private void showGirls() {
        List<Student> students = studentTable.selectAllByColumn("sex", Gender.FEMALE.toString());
        String separator = "+" + String.join("", Collections.nCopies(59, "-")) + "+";

        System.out.println(separator);
        System.out.printf("| %-4s | %-50s |\n", "ID", "ФИО");
        System.out.println(separator);
        for (Student student : students) {
            System.out.printf("| %-4d | %-50s |\n", student.getId(), student.getFullName());
        }
        System.out.println(separator);
    }

    private void showGroups() {
        String separator = "+" + String.join("", Collections.nCopies(70, "-")) + "+";

        System.out.println(separator);
        System.out.printf("| %-4s | %-8s | %-50s |\n", "ID", "Группа", "Куратор");
        System.out.println(separator);

        String query = String.format(
                "select g.id, g.name, c.fio " +
                        "from `%s` as g " +
                        "join `%s` as c on c.id = g.id_curator",
                groupTable.getName(), curatorTable.getName());
        try (ResultSet result = dbExecutor.executeQuery(query)) {
            while (result.next()) {
                System.out.printf("| %-4d ", result.getInt(1));
                System.out.printf("| %-8s ", result.getString(2));
                System.out.printf("| %-50s |\n", result.getString(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(separator);
    }

    private void showGroup() {
        Group group = getGroup();
        if (group == null)
            return;

        String separator = "+" + String.join("", Collections.nCopies(69, "-")) + "+";

        System.out.println(separator);
        System.out.printf("| %-4s | %-50s | %-7s |\n", "ID", "ФИО", "Пол");
        System.out.println(separator);

        String query = String.format("select * from `%s` where id_group = (select id from `%s` where `name` = '%s')",
                studentTable.getName(), groupTable.getName(), group.getName());
        try (ResultSet result = dbExecutor.executeQuery(query)) {
            while (result.next()) {
                System.out.printf("| %-4d ", result.getInt(1));
                System.out.printf("| %-50s ", result.getString(2));
                System.out.printf("| %-7s |\n", result.getString(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(separator);
    }

    private void changeGroupCurator() {
        Group group = getGroup();
        if (group == null)
            return;

        Curator curator = getCurator();
        if (curator == null)
            return;

        String query = String.format("update `%s` set id_curator = %d where id = %d",
                groupTable.getName(), curator.getId(), group.getId());
        dbExecutor.execute(query);
        System.out.println("Куратор группы изменен.");
    }

    private Curator getCurator() {
        String curatorName = Console.getString("ФИО куратора");
        Curator curator = curatorTable.selectItemByColumn("fio", curatorName);
        if (curator == null) {
            System.out.println("Указанный куратор не существует. Добавьте сначала куратора.");
        }
        return curator;
    }

    private Group getGroup() {
        String groupName = Console.getString("Имя группы");
        Group group = groupTable.selectItemByColumn("name", groupName);
        if (group == null) {
            System.out.println("Указанной группы не существует. Добавьте сначала группу.");
        }
        return group;
    }

    private Gender getGender() {
        while (true) {
            try {
                return Gender.fromString(Console.getString("Пол студента"));
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: неверный пол.");
                System.out.println("Допустимые значения: женский, жен, ж, female, f, мужской, муж, м, male, m.");
            }
        }
    }

    @Override
    public void close() {
        studentTable.delete();
        System.out.printf("Таблица '%s' удалена.\n", studentTable.getName());
        groupTable.delete();
        System.out.printf("Таблица '%s' удалена.\n", groupTable.getName());
        curatorTable.delete();
        System.out.printf("Таблица '%s' удалена.\n", curatorTable.getName());
        dbExecutor.close();
    }
}

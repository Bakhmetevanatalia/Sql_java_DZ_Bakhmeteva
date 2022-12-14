package data;

public class Group {
    private final int id;
    private final String name;
    private final int curatorId;

    public Group(int id, String name, int curatorId) {
        this.id = id;
        this.name = name;
        this.curatorId = curatorId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCuratorId() {
        return curatorId;
    }
}

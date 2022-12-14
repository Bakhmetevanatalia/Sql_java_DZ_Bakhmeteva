package data;

public class Curator {
    private final int id;
    private final String fullName;

    public Curator(int id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return "Curator{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}

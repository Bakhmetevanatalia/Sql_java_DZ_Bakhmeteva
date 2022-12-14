package data;

public class Student {
  private final int id;
  private final String fullName;
  private final Gender gender;
  private final int groupId;

  public Student(int id, String fullName, Gender gender, int groupId) {
    this.id = id;
    this.fullName = fullName;
    this.gender = gender;
    this.groupId = groupId;
  }

  public int getId() {
    return id;
  }

  public String getFullName() {
    return fullName;
  }

  public Gender getGender() {
    return gender;
  }

  public int getGroupId() {
    return groupId;
  }

}

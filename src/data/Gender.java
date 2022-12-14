package data;

public enum Gender {
    FEMALE("женский"),
    MALE("мужской");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Gender fromString(String value) {
        for (Gender gender : values()) {
            String gValue = gender.toString();
            String gName = gender.name();
            // позволяем следующие значения: женский, жен, ж, female, f, мужской, муж, м, male, m
            if (value.equalsIgnoreCase(gValue) || value.equalsIgnoreCase(gName) ||
                    value.equalsIgnoreCase(gValue.substring(0, 3)) ||
                    value.equalsIgnoreCase(gValue.substring(0, 1)) ||
                    value.equalsIgnoreCase(gName.substring(0, 1)))
            {
                return gender;
            }
        }
        throw new IllegalArgumentException("Gender can't be " + value);
    }
}

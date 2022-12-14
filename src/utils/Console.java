package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static int getInt(String prompt, int min, int max) {
        while (true) {
            try {
                int result = Integer.parseInt(getString(prompt));
                if (result >= min && result <= max)
                    return result;

                if (max < Integer.MAX_VALUE)
                    System.out.printf("Ошибка: Недопустимое число: ожидалось значение от %d до %d.\n", min, max);
                else
                    System.out.printf("Ошибка: Недопустимое число: ожидалось значение >= %d.\n", min);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Неверный формат: ожидалось целое число.");
            }
        }
    }

    public static String getString(String prompt) {
        while (true) {
            try {
                System.out.printf("%s: ", prompt);
                String result = reader.readLine().trim();
                if (!result.isEmpty())
                    return result;
                System.out.println("Ошибка: Неверный ввод: значение не может быть пустым.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void pause() {
        System.out.println("Нажмите <Enter> для продолжения...");
        try {
            reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

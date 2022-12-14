package ui;


import utils.Console;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AbstractAction {
    private final List<Action> actions;
    private final boolean isTopLevel;

    private Menu(String description, boolean isTop) {
        super(description);
        actions = new ArrayList<>();
        isTopLevel = isTop;
    }

    public static Menu create(String desc) {
        return new Menu(desc, true);
    }

    public static Menu createSubMenu(String desc) {
        return new Menu(desc, false);
    }

    public Menu addAction(String description, Action action) {
        actions.add(new MenuAction(description, action));
        return this;
    }

    public Menu addAction(Menu subMenu) {
        actions.add(subMenu);
        return this;
    }

    @Override
    public void execute() {
        while (true) {
            System.out.println(this);
            for (int i = 1; i <= actions.size(); i++) {
                System.out.printf("%d. %s\n", i, actions.get(i - 1));
            }
            System.out.println(isTopLevel ? "0. Exit" : "0. Back");

            int choice = Console.getInt("Ваш выбор", 0, actions.size());
            System.out.println();

            if (choice == 0)
                break;

            Action action = actions.get(choice - 1);
            action.execute();

            if (!(action instanceof Menu))
                Console.pause();
        }
    }

}

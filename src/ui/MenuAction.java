package ui;

public class MenuAction extends AbstractAction {
    private final Action action;

    public MenuAction(String description, Action action) {
        super(description);
        this.action = action;
    }

    @Override
    public void execute() {
        System.out.println(this);
        action.execute();
        System.out.println();
    }
}

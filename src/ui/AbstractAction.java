package ui;


public abstract class AbstractAction implements Action {
    private final String description;

    public AbstractAction(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}

public class Task {
    private String name;
    private boolean check;

    public void setName(String name) {
        this.name = name;
    }

    public void setCheck(String symbol) {
        this.check = symbol.equals("X");
    }

    public String getName() {
        return name;
    }

    public boolean getCheck() {
        return check;
    }

    @Override
    public String toString() {
        return ((getCheck() ? "X" : "O")+" - "+getName());
    }

    public String toStringForSave() {
        return ((getCheck() ? "X" : "O") + ";" + getName() + "\n");
    }
}

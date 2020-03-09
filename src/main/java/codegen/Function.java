package codegen;

public class Function{
    private String name;

    public Function(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Function{" +
                "name='" + name + '\'' +
                '}';
    }
}

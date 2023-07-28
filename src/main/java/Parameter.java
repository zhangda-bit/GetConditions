import java.util.ArrayList;
import java.util.List;

public class Parameter {
    private String name;
    private String type;
    private List<String> values;


    public Parameter() {
        values = new ArrayList<>();
    }

    public Parameter(String name, String type, List<String> values) {
        this.name = name;
        this.type = type;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", values=" + values +
                '}';
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}

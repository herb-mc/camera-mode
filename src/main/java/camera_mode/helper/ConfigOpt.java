package camera_mode.helper;

public class ConfigOpt {

    private Object value;

    public ConfigOpt(Object in) {
        value = in;
    }

    public void setValue(Object in) {
        value = in;
    }

    public Object getValue() {
        return value;
    }

    public int getInt() {
        return (int) value;
    }

    public boolean getBool() {
        return (boolean) value;
    }

    public String getAsString() {
        return value.toString();
    }

}
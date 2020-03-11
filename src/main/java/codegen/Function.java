package codegen;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Function{
    @JsonIgnore
    private String name;
    private String handler;
    private String language;
    private String code;

    public Function(String name, String handler, String language, String code) {
        this.name = name;
        this.handler = handler;
        this.language = language;
        this.code = code;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
                ", handler='" + handler + '\'' +
                ", language='" + language + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}

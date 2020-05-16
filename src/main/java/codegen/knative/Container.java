package codegen.knative;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Container {
    @JsonProperty("image")
    private String image;

    @JsonProperty("env")
    private ArrayList<Env> env = new ArrayList<>();

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Env> getEnv() {
        return env;
    }

    public void setEnv(ArrayList<Env> env) {
        this.env = env;
    }
}

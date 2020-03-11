package codegen.aws.models.formation.lambda;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LambdaProperty {
    @JsonProperty("Handler")
    private String handler;
    @JsonProperty("Role")
    private LambdaRole role;
    @JsonProperty("Code")
    private LambdaCode code;
    @JsonProperty("Runtime")
    private String runtime;
    @JsonProperty("Timeout")
    private String timeout;

    public LambdaProperty(String handler, LambdaRole role, LambdaCode code, String runtime,
                          String timeout) {
        this.handler = handler;
        this.role = role;
        this.code = code;
        this.runtime = runtime;
        this.timeout = timeout;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public LambdaRole getRole() {
        return role;
    }

    public void setRole(LambdaRole role) {
        this.role = role;
    }

    public LambdaCode getCode() {
        return code;
    }

    public void setCode(LambdaCode code) {
        this.code = code;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}

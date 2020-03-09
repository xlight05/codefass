package codegen.aws.models.formation.lambda;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LambdaCode {
    @JsonProperty("ZipFile")
    private String zipFile;

    public LambdaCode(String zipFile) {
        this.zipFile = zipFile;
    }

    public String getZipFile() {
        return zipFile;
    }

    public void setZipFile(String zipFile) {
        this.zipFile = zipFile;
    }
}

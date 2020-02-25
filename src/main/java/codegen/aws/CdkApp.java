package codegen.aws;

import codegen.aws.CdkStack;
import software.amazon.awscdk.core.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CdkApp {
    public CdkApp (){
        App app = new App();

        new CdkStack(app, "CdkStack");

        app.synth();

        final ProcessBuilder processBuilder = new ProcessBuilder
                ("/bin/bash", "-ic", "cdk synth");

        processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        try {
            processBuilder.start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

//        Runtime rt = Runtime.getRuntime();
//        String[] cmdArr = new String[]{"/bin/bash", "-c", "cdk synth > target/infa.yaml"};
//        Process proc = rt.exec(cmdArr);
//        InputStream stderr = proc.getErrorStream();
//        InputStreamReader isr = new InputStreamReader(stderr);
//        BufferedReader br = new BufferedReader(isr);
//        String line = null;
//        System.out.println("<ERROR>");
//        while ( (line = br.readLine()) != null)
//            System.out.println(line);
//        System.out.println("</ERROR>");
//        int exitVal = proc.waitFor();
//        System.out.println("Process exitValue: " + exitVal);
    }
}

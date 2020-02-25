package cli;

import codegen.aws.CdkApp;
import picocli.CommandLine;

@CommandLine.Command(name = "aws")
public class FassBuildAws implements Runnable{
    public void run() {
        System.out.println("Building AWS shit");
        CdkApp app = new CdkApp();
    }
}

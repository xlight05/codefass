package cli;

import picocli.CommandLine;

@CommandLine.Command(name = "knative")
public class FassBuildKnative implements Runnable{
    public void run() {
        System.out.println("Building knative shit");
    }
}
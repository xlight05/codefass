package cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "build",
        subcommands = {
                FassBuildAws.class,
                FassBuildKnative.class,
        })
public class FassBuild implements Runnable {
    public void run() {
        System.err.println("Please execute one of the following commands");
        new CommandLine(this).usage(System.out);
    }
}
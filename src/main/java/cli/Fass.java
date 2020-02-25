package cli;

import picocli.CommandLine;

@CommandLine.Command(name = "fass",
        mixinStandardHelpOptions = true,
        subcommands = {
        FassBuild.class
    })
public class Fass implements Runnable {
    public static void main(String[] args) {
        // By implementing Runnable or Callable, parsing, error handling and handling user
        // requests for usage help or version help can be done with one line of code.
        CommandLine commandLine = new CommandLine(new Fass());
        commandLine.parseWithHandler(new CommandLine.RunLast(), args);
    }
    @Override
    public void run() {
        System.err.println("Please execute one of the following commands");
        new CommandLine(this).usage(System.out);
    }
}

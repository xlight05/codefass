package cli;

import org.codehaus.plexus.util.FileUtils;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

import static cli.FassBuildAws.listFilesForFolder;

@CommandLine.Command(name = "knative")
public class FassBuildKnative implements Runnable{
    public void run() {
        final File tmp = new File("");
        final File folder = new File(tmp.getAbsolutePath());
        String defFilePath = tmp.getAbsolutePath()+File.separator+listFilesForFolder(folder);
        System.out.println("Compiling: "+ defFilePath);

        String source = "/home/xlight/tmp-art/knative/";
        File srcDir = new File(source);

        String destination = "output/knative";
        File destDir = new File(destination);
        System.out.println("Compilation complete");

        try {
            FileUtils.copyDirectory(srcDir, destDir);
            System.out.println("Knative Artifacts generated: "+folder+"/output/knative");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
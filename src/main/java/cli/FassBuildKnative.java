package cli;

import codegen.CloudArtifactGenerator;
import codegen.FunctionOrchestrator;
import codegen.aws.models.formation.CloudFormationGenerator;
import codegen.knative.KnativeGenerator;
import compiler.Executor;
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

        Executor compiler = new Executor();
        try {
            FunctionOrchestrator liveFlow = compiler.compile(defFilePath);
            //System.out.println(liveFlow);
            CloudArtifactGenerator knativeGen = new KnativeGenerator(liveFlow);
            knativeGen.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
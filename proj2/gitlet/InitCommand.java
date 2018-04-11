package gitlet;

import java.io.File;
import java.util.ArrayList;

public class InitCommand {
    public InitCommand() {

    }

    public static boolean checkIfInitialized() {
        File f = new File("./.gitlet");
        return (f.exists());
    }
    public static void execute(String[] args) {
        if (args.length != 1) {
            System.err.println("Incorrect operands.");
            System.exit(0);
        }
        File f = new File("./.gitlet");
        if (f.exists()) {
            f.mkdir();
            System.out.println(
                    "A gitlet version-control system already exists in the current directory.");
        } else {
            f.mkdir();
        }
        //initialize our empty objects
        StagingArea a = new StagingArea();
        CommitTree t = new CommitTree();
        Commit initial = new Commit(null, "initial commit", new ArrayList<String>());
        Branch master = t.createBranch("master", initial);
        t.setCurrBranch(master);

        //serialize the first commit, the commit tree, the staging area
        initial.serialize("./.gitlet/" + initial.getID());
        t.serialize("./.gitlet/commitTree");
        a.serialize("./.gitlet/stagingArea");
        System.exit(0);
    }
}

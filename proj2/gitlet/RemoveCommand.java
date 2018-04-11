package gitlet;

import java.io.File;

public class RemoveCommand {

    public static void execute(String[] args, String fileName, CommitTree t, StagingArea staging) {
        if (args.length != 2) {
            System.err.println("Incorrect operands.");
            System.exit(0);
        }
        Commit head = t.getCurrBranch().getHeadCommit();
        if (head.getFileMap().containsKey(fileName)) { //if the file is in the head commit
            staging.addRemovedFile(fileName);
            File f = new File(fileName);
            f.delete(); //delete the file from the working directory
            return;
        }
        if (staging.isInStaging(fileName)) {
            staging.unstage(fileName); //remove from staging area
            return;
        } else if (!staging.isInStaging(fileName) && !head.getFileMap().containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
        } else {
            //do nothing

            return;
        }
    }

}

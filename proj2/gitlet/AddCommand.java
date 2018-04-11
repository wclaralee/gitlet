package gitlet;
import java.io.File;
import java.util.HashMap;

public class AddCommand {

    public AddCommand() {

    }
    public static void execute(String[] args, StagingArea a, CommitTree tree, String filename) {
        if (args.length != 2) {
            System.err.println("Incorrect operands.");
            System.exit(0);
        }
        File f = new File(filename);
        if (f.exists()) {

            //Serialize, check if this has been commited before with same id
            Blob b = new Blob(filename);
            String hash = b.getHashId(); //new hash
            HashMap<String, String> maps = tree.getCurrBranch().getHeadCommit().getFileMap();


            if (a.isInRemoved(filename)
                    && tree.getCurrBranch().getHeadCommit().getFileMap().containsKey(filename)) {
                //see remove-add-status:
                //this is the "unremove" code
                a.getRemovedFiles().remove(filename); //readding, removing from the file names
                maps.put(filename, hash);
                return;
            }
            if (maps.containsKey(filename) && maps.containsValue(hash)) {
                return;
            } else if (a.isInStaging(filename)) {
                //System.out.print("already added this file");
                return;
            } else {
                a.stage(filename);
                return;
            }
        } else {
            System.out.print("File does not exist.");
            return;
        }
    }

}

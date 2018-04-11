package gitlet;

import java.io.File;
import java.util.HashMap;

public class CheckoutCommand {

    public static void serializeStructures(CommitTree t, StagingArea s) {
        //System.out.println("structures serialized");
        s.serialize(CheckoutCommand.formatFilePath("stagingArea"));
        t.serialize(CheckoutCommand.formatFilePath("commitTree"));
    }
    public static String formatFilePath(String flatPath) {
        return "./.gitlet/" + flatPath;
    }

    public static void runCheckout(String[] args, CommitTree t, StagingArea s) {
        if (args.length == 3 && args[1].equals("--")) {
            CheckoutCommand.checkoutFile(t, args[2]);
            CheckoutCommand.serializeStructures(t, s);
            System.exit(0);
        } else if (args.length == 4 && args[2].equals("--")) {
            CheckoutCommand.checkoutFileInCommit(args[1], args[3]);
            CheckoutCommand.serializeStructures(t, s);
            System.exit(0);
        } else if (args.length == 2) {
            if (args[1] != null && args[1].length() > 1) { //some error checking
                CheckoutCommand.checkoutBranch(t, s, args[1]);
                CheckoutCommand.serializeStructures(t, s);
                System.exit(0);
            }
        } else {
            System.err.println("Incorrect operands.");
            System.exit(0);
        }
    }
    public static void checkoutFile(CommitTree t, String fileName) {
        Commit c = t.getCurrBranch().getHeadCommit();
        HashMap<String, String> hash = c.getFileMap();

        if (hash.containsKey(fileName)) {
            String path = "./.gitlet/" + hash.get(fileName);
            Blob b = Blob.deserialize(path);
            File f = new File("./" + fileName);
            Utils.writeContents(f, b.getFileContents());
            //finished!
        }
        c.serialize("./.gitlet/" + c.getID());

    }
    public static void checkoutFileInCommit(String commitName, String fileName) {
        File dir = new File("./.gitlet");
        File[] files = dir.listFiles();
        boolean foundCommit = false;
        for (File f: files) {
            //check equality
            String abbrev = "";
            if (f.getName().length() > 7) {
                abbrev = f.getName().substring(0, 8);
            }
            if (f.getName().equals(commitName) || abbrev.equals(commitName)) {
                //found it
                foundCommit = true; //found a commit
                String runPath = "./.gitlet/" + f.getName();
                Commit c = Commit.deserialize(runPath);
                HashMap<String, String> hash = c.getFileMap();
                if (hash.containsKey(fileName)) {
                    String path = "./.gitlet/" + hash.get(fileName);
                    Blob b = Blob.deserialize(path);
                    //delete for safety
                    String pathname = "./" + fileName; //why?
                    File newF = new File(pathname);
                    newF.delete();
                    Utils.writeContents(newF, b.getFileContents());
                    //serialize the commit and the blob again
                    b.serialize("./.gitlet/" + b.getHashId());
                    c.serialize("./.gitlet/" + c.getID());
                    //finished!
                } else {
                    System.out.println("File does not exist in that commit.");
                }
            }
        }
        if (!foundCommit) {
            System.out.println("No commit with that id exists.");
        }
        //nothing found
    }

    public static void checkoutBranch(CommitTree t, StagingArea staging, String branchName) {
        //damn this ones a little trickier
        Commit trackedCommit = t.getCurrBranch().getHeadCommit();

        Branch foundBranch = null;
        for (Branch b : t.getBranches()) {
            if (b.getName().equals(branchName)) {
                foundBranch = b;
                break;
            }
        }
        if (foundBranch == null) {
            System.out.println("No such branch exists.");
            return;
        }
        if (foundBranch.getName().equals(t.getCurrBranch().getName())) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        //else continue onward
        Commit c = foundBranch.getHeadCommit(); //deserialized, or it will break
        HashMap<String, String> map = c.getFileMap();
        //write all the new files
        for (String key: map.keySet()) {
            String path = "./.gitlet/" + map.get(key); //read path
            Blob b = Blob.deserialize(path);
            File f = new File("./" + key); //write path

            if (f.exists() && !trackedCommit.getFileMap().containsKey(key)) { //if untracked
                //file exists and is in the untracked directory
                System.out.println("There is an untracked file in the way; "
                        + "delete it or add it first.");
                return;
            } else {
                Utils.writeContents(f, b.getFileContents());
            }
        }
        //now do all the deleting, of files that are untracked by the current branch
        for (String key : trackedCommit.getFileMap().keySet()) {
            if (!c.getFileMap().containsKey(key)) {
                File f = new File("./" + key);
                f.delete();
            }
        }
        trackedCommit.serialize("./.gitlet/" + trackedCommit.getID());
        t.setCurrBranch(foundBranch);
        c.serialize("./.gitlet/" + c.getID());
        staging.clear(); //clear, made it to the end
    }

}

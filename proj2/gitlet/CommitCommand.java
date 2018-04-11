package gitlet;

import java.util.ArrayList;
import java.util.HashMap;
public class CommitCommand {


    public static void serializeStructures(CommitTree t, StagingArea s) {
        //System.out.println("structures serialized");
        s.serialize(CommitCommand.formatFilePath("stagingArea"));
        t.serialize(CommitCommand.formatFilePath("commitTree"));
    }
    public static String formatFilePath(String flatPath) {
        return "./.gitlet/" + flatPath;
    }

    public static void runCommit(String[] args, CommitTree t, StagingArea staging) {
        if (args.length > 2) {
            System.err.println("Incorrect operands.");
        } else if (args.length < 2) {
            System.out.println("Please enter a commit message.");
        } else if (args[1].length() < 1) {
            System.out.println("Please enter a commit message.");
        } else {
            execute(t, args[1], staging);
            serializeStructures(t, staging);
        }
        System.exit(0);
    }

    public static void execute(CommitTree t, String message, StagingArea staging) {
        if (staging.isEmpty()) {
            //for weird edge case in the find, when you rm then add again
            System.out.println("No changes added to the commit.");
            return;
        }
        //message
        Commit currHead = t.getCurrBranch().getHeadCommit();
        Commit m = new Commit(currHead, message, staging.getStagedFileNames());
        //serialize commit
        ArrayList<String> files = staging.getStagedFileNames();
        HashMap<String, String> hashes = new HashMap<String, String>();
        ArrayList<String> removed = staging.getRemovedFiles();

        //BAD this is decoupled from the commit serialization
        for (int i = 0; i < files.size(); i++) {
            //iterate through staging area
            Blob b = new Blob(files.get(i));
            hashes.put(files.get(i), b.getHashId());
            String blobPath = "./.gitlet/" + b.getHashId();
            b.serialize(blobPath); //serialize the new blobs
        }
        for (int j = 0; j < removed.size(); j++) {
            m.getFileMap().remove(removed.get(j)); //remove from file map
        }
        //serialize parent
        String parentHash = "./.gitlet/" + m.getID();
        currHead.serialize(parentHash); //serialize parent


        String commitPath = "./.gitlet/" + m.getID();
        String commitId = m.serialize(commitPath); //serialize commit

        staging.clear(); //should clear the staging area

        //set the head of the current branch
        Branch b = t.getCurrBranch();
        b.setHeadCommit(m);

    }
}

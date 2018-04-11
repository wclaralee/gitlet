package gitlet;
import java.io.File;
import java.util.HashMap;

public class ResetCommand {


    
    public static void execute(String commitID, CommitTree t, StagingArea staged) {
        //is there an arrayList of commit IDs?
        //check for untracked files
        //non abbreviated
        File dir = new File("./.gitlet/");
        File[] files = dir.listFiles();
        Commit c = null;
        for (File f : files) {
            if (f.getName().equals(commitID)) {
                c = Commit.deserialize("./.gitlet/" + f.getName());
            }
        }
        //abbreviate reset
        for (File f : files) {
            if (f.getName().substring(0, 6).equals(commitID)) {
                c = Commit.deserialize("./.gitlet/" + f.getName());
            }
        }
        if (c == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        //iterate through the files, checking for untracked files
        //return if something will be unwritten
        File dir2 = new File("./");
        File[] workingFiles = dir2.listFiles();
        HashMap<String, String> commitMap = c.getFileMap();
        for (File f: workingFiles) {
            if (commitMap.containsKey(f.getName())) {
                Blob hashed = new Blob(f.getName()); //temp blob to check contents
                if (!hashed.getHashId().equals(commitMap.get(f.getName()))) {
                    System.out.println("There is an untracked file in the way; delete it or add it first.");
                    return;
                }
            }
        }
        //delete the files not tracked in the current commit
        HashMap<String, String> headMap = t.getCurrBranch().getHeadCommit().getFileMap();
        for (String key : headMap.keySet()) {
            if (!commitMap.containsKey(key)) {
                File f = new File(key);
                f.delete();
            }
        }
        //now iterate through the commit maps files
        for (String key : commitMap.keySet()) {
            //deserialize the saved blobs
            Blob b = Blob.deserialize("./.gitlet/" + commitMap.get(key));
            File f = new File("./" + key);
            f.delete();
            //write the new files into the working directory
            Utils.writeContents(f, b.getFileContents());
        }
        staged.clear(); //clear staging
        t.getCurrBranch().setHeadCommit(c); //set the current branch head
        //continue to the replacement and writing


    }

}

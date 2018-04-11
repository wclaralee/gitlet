package gitlet;
import java.util.ArrayList;
import java.io.File;

public class StatusCommand {

    public static void execute(String[] args, StagingArea staged, CommitTree t) {
        ArrayList<String> filenames;
        ArrayList<Branch> allBranches;
        ArrayList<String> remFiles;
        if (args.length != 1) {
            System.err.println("Incorrect operands.");
            System.exit(0);
        }
        //prevent null pointer errors
        if (staged == null) {
            filenames = new ArrayList<String>();
            remFiles = new ArrayList<String>();
        } else {
            filenames = staged.getStagedFileNames();
            remFiles = staged.getRemovedFiles();
        }
        if (t == null) {
            allBranches = new ArrayList<Branch>();
        } else {
            allBranches = t.getBranches();
        }
        System.out.println("=== Branches ===");
        for (int i = 0; i < allBranches.size(); i++) {
            if (allBranches.get(i).getName().equals(t.getCurrBranch().getName())) {
                String toPrint = "*" + allBranches.get(i).getName();
                System.out.println(toPrint);
            } else {
                System.out.println(allBranches.get(i).getName());
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (int i = 0; i < filenames.size(); i++) {
            System.out.println(filenames.get(i));
        }
        System.out.println();
        System.out.println("=== Removed Files ===");

        for (int i = 0; i < remFiles.size(); i++) {
            System.out.println(remFiles.get(i));
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        printUntrackedFiles(staged, t);
        System.out.println();
        System.exit(0);
    }
    private static void printUntrackedFiles(StagingArea staged, CommitTree t) {
        //? 
        File dir = new File("./");
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.getName().contains(".txt")) {
                //if .txt, and not a staged file, it's unstaged, and not in a commit
                //probably will have to check if it's also been modified fuck
                if (!staged.isInStaging(f.getName())
                        && !t.getCurrBranch().getHeadCommit().getFileMap()
                        .containsKey(f.getName())) {
                    System.out.println(f.getName());
                }
            }
        }
        System.exit(0);
    }
}


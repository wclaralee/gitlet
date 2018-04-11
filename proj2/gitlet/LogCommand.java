package gitlet;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LogCommand {

    public static void execute(String[] args, CommitTree t) {
        if (args.length != 1) {
            System.err.println("Incorrect operands.");
            System.exit(0);
        }

        Commit currCommit = t.getCurrBranch().getHeadCommit();
        while (true) { //weird exit
            System.out.println("===");
            System.out.println("Commit " + currCommit.getID());
            Date d = currCommit.getCommitDate();
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatted = outputFormat.format(d);
            System.out.println(formatted); // resolve time formatting
            System.out.println(currCommit.getMessage());
            System.out.println();
            if (currCommit.getParentHash().length() < 2) {
                //last one, return
                System.exit(0);
            }
            Commit newC = currCommit.getLast(); // standardize name for commitParent
            currCommit.serialize("./.gitlet/" + currCommit.getID());
            currCommit = newC;
        }
    }
}

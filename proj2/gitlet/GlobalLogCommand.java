package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GlobalLogCommand {
    public static void execute(String[] args) {
        if (args.length != 1) {
            System.err.println("Incorrect operands.");
            System.exit(0);
        }
        File dir = new File("./.gitlet/");
        File[] files = dir.listFiles();
        for (File f : files) {
            if (!f.getName().contains(".txt")) {
                Commit c = null;
                try {
                    c = Commit.deserialize("./.gitlet/" + f.getName());
                } catch (Exception e) {
                    //do nothing
                }
                if (c != null) {
                    //if it worked
                    String currID = c.getID();
                    System.out.println("===");
                    System.out.println("Commit " + currID);
                    Date d = c.getCommitDate();
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formatted = outputFormat.format(d);
                    System.out.println(formatted); // resolve time formatting
                    System.out.println(c.getMessage());
                    System.out.println();
                }
            }
        }
        System.exit(0);
    }
}

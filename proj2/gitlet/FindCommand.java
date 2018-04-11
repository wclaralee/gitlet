package gitlet;

import java.io.File;

public class FindCommand {
    public static void execute(String message) {
        // iterate through branch heads up until split points
        File dir = new File("./.gitlet/");
        File[] files = dir.listFiles();
        boolean foundOne = false;
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
                    if (c.getMessage().equals(message)) {
                        System.out.println(c.getID());
                        foundOne = true;
                    }
                }
            }
        }
        if (!foundOne) {
            System.out.println("Found no commit with that message.");
            return;
        }
    }
}

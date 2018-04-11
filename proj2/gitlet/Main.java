package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  
 */
public class Main {
    //deserialized structures
    static CommitTree commitTree;
    static StagingArea stagingArea;
    static boolean initialized = false; //check if init has been called and .gitlet exists
    public static void main(String[] args) {
        initialized = InitCommand.checkIfInitialized();
        if (initialized) { initStructures(); }
        if (args.length == 0) { System.err.println("Please enter a command."); System.exit(0); }
        if (args[0].equals("init")) { InitCommand.execute(args); }
        if (!initialized) { System.err.println("Not in an initialized gitlet directory."); System.exit(0); }
        if (args[0].equals("add")) {
            AddCommand.execute(args, stagingArea, commitTree, args[1]);
            serializeStructures();
            System.exit(0);
        }
        if (args[0].equals("rm")) {
            RemoveCommand.execute(args, args[1], commitTree, stagingArea);
            serializeStructures();
            System.exit(0);
        }
        if (args[0].equals("status")) { StatusCommand.execute(args, stagingArea, commitTree); }
        if (args.length >= 1 && args[0].equals("branch")) {
            BranchCommand.execute(args, args[1], commitTree);
            serializeStructures();
            System.exit(0);
        }
        if (args.length >= 1 && args[0].equals("checkout")) { CheckoutCommand.runCheckout(args, commitTree, stagingArea); }
        if (args[0].equals("log")) { LogCommand.execute(args, commitTree); }
        if (args[0].equals("commit")) {
            CommitCommand.runCommit(args, commitTree, stagingArea);
        }
        if (args[0].equals("global-log")) { GlobalLogCommand.execute(args); }
        if (args.length >= 1 && args[0].equals("reset")) {
            ResetCommand.execute(args[1], commitTree, stagingArea);
            serializeStructures();
            System.exit(0);
        }
        /*if (args[0].equals("merge") && args.length >= 1) {
            mergeCommand.execute(commitTree, stagingArea, args[1]);
            serializeStructures();
            System.exit(0);
        }*/
        if (args[0].equals("find") && args.length >= 1) {
            FindCommand.execute(args[1]);
            serializeStructures();
            System.exit(0);
        }
        System.err.println("No command with that name exists.");
        System.exit(0);
    }
    public static void initStructures() {
        //System.out.println("structures initialized");
        stagingArea = StagingArea.deserialize(Main.formatFilePath("stagingArea"));
        commitTree = CommitTree.deserialize(Main.formatFilePath("commitTree"));
    }
    public static void serializeStructures() {
        if (initialized) {
            //System.out.println("structures serialized");
            stagingArea.serialize(Main.formatFilePath("stagingArea"));
            commitTree.serialize(Main.formatFilePath("commitTree"));
        }
    }
    public static String formatFilePath(String flatPath) {
        return "./.gitlet/" + flatPath;
    }
}

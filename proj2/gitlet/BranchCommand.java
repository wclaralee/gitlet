package gitlet;

public class BranchCommand {
    // should be completed basically
    public static void execute(String[] args, String branchName, CommitTree t) {
        if (args.length < 2) {
            System.exit(0);
        } else if (args[1].length() < 1) {
            System.exit(0);
        }
        //if branch with the name already exists, throws error
        for (Branch b : t.getBranches()) {

            if (b.getName().equals(branchName)) {
                System.out.println("A branch with that name already exists.");
                return;
            }
        }
        Branch newBranch = new Branch(branchName, t.getCurrBranch().getHeadCommit());
        // added splits condition (see global-log / find)
        t.getSplits().add(t.getCurrBranch().getHeadCommit().getID());
        t.getBranches().add(newBranch);
    }
}


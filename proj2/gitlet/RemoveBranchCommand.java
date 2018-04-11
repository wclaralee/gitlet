package gitlet;

public class RemoveBranchCommand {

    public void execute(CommitTree t, String branchName) {
        if (t.getCurrBranch().getName().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            return;
        } else if (!t.getBranches().contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        } else {
            t.getBranches().remove(branchName);
            return;
        }
    }
}


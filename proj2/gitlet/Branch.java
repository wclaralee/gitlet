package gitlet;

import java.io.Serializable;


public class Branch implements Serializable {
    private String name;
    private Commit headCommit;


    public Branch(String branchName, Commit headCommit) {
        this.name = branchName;
        this.headCommit = headCommit;
    }

    public Commit getHeadCommit() {
        return this.headCommit;
    }

    public String getName() {
        return this.name;
    }
    public void setHeadCommit(Commit commit) {
        headCommit = commit;
    }
}

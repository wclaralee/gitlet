
package gitlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class CommitTree implements Serializable {

    private ArrayList<Branch> branches; //hold pointers to each Branch
    private ArrayList<String> splits; //hashIDs of all the branch splits
    private Branch currBranch;

    public CommitTree() {
        branches = new ArrayList<Branch>();
        splits = new ArrayList<String>();
    }

    //return a split
    public ArrayList<String> getSplits() {
        return this.splits;
    }

    public String getSplit(String hashName) {
        if (splits.contains(hashName)) {
            return splits.get(splits.indexOf(hashName));
        } else {
            return null;
        }
    }

    public void addSplit(String hashName) {

        splits.add(hashName);
    }

    public Branch getBranch(String branchname) {
        for (int i = 0; i < branches.size(); i++) {
            if (this.branches.get(i).getName().equals(branchname)) {
                return branches.get(i);
            }
        }
        //if not found
        System.err.println("No such branch exists.");
        return null;
    }

    public ArrayList<Branch> getBranches() {
        return this.branches;
    }

    public Branch createBranch(String branchName, Commit head) {
        Branch b = new Branch(branchName, head);
        branches.add(b);
        return b;
    }
    public Branch getCurrBranch() {
        return this.currBranch;
    }
    public void setCurrBranch(Branch b) {

        this.currBranch = b;
    }

    public void serialize(String pathName) {
        File commitTree = new File(pathName);
        if (commitTree.exists()) {
            commitTree.delete();
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(commitTree));
            out.writeObject(this);
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error in commit tree serialization");
        } catch (IOException e) {
            System.out.println("Error in commit tree serialization.");
        }
        return;
    }

    public static CommitTree deserialize(String pathName) {
        File commitTree = new File(pathName);
        CommitTree returned = null;
        try {
            ObjectInputStream input = new ObjectInputStream((new FileInputStream(commitTree)));
            returned = (CommitTree) input.readObject();
            input.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error in commit tree deseriaization, file not found");
        } catch (Exception e) {
            System.out.println("Error in commit tree deserialization");
        }
        return returned;
    }
}

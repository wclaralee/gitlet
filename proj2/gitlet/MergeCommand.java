package gitlet;

import java.util.*;
import java.io.*;
import java.lang.*;
import java.io.ByteArrayOutputStream;

public class MergeCommand {

    public void execute(CommitTree tree, StagingArea staging, String givenbranchname) {

        //weird catch cases
        if (!staging.isEmpty()){
            System.out.println("You have uncommitted changes.");
            return;
        }
        Branch b = null;
        ArrayList<Branch> branches = tree.getBranches();
        for (Branch searched: branches){
            if (searched.getName().equals(givenbranchname)){
                b = searched;
                break;
            }
        }
        if (b == null){
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (tree.getCurrBranch().getName().equals(givenbranchname)){
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        //if branch has
        File dir = new File("./");
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.getName().contains(".txt")) {
                if (b.getHeadCommit().getFileMap().containsKey(f.getName())) {
                    System.out.println("There is an untracked file in the way; delete it or add it first.");
                    return;
                }
            }
        }
        //continue on and find my split
        Commit thisHead = tree.getCurrBranch().getHeadCommit();
        Commit thatHead = b.getHeadCommit();
        Commit split;
        ArrayList<Commit> ones = new ArrayList<Commit>(); //store em
        Commit oneIt = thatHead;
        while (oneIt.getParentHash().length() > 2){
            ones.add(oneIt);
            oneIt.getLast();
            //check if fastfoward
            if( oneIt.getID().equals(thisHead.getID())) { //if it was the parent
                System.out.println("Given branch is an ancestor of the current branch");
                return;
            }
        }
        Commit twoIt = thisHead;
        while (twoIt.getParentHash().length() > 2){
            if (twoIt.getID().equals(thatHead.getID())){
                System.out.println("Current branch fast-forwarded.");
                tree.setCurrBranch(b);
                return;
            }
            if (ones.contains(twoIt)){
                split = twoIt; //has to be the same branch
                break;
            }
            twoIt = twoIt.getLast();
        }

        //alrighty moving on, now we have a split point

    }
    private void overwriteFile(String filepath, byte[] file1, byte[] file2){
        byte[] head = "<<<<<<< HEAD".getBytes();
        byte[] mid = "=======".getBytes();
        byte[] end = ">>>>>>>".getBytes();
        File f = new File(filepath);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            outputStream.write(head);
            outputStream.write(file1);
            outputStream.write(mid);
            outputStream.write(file2);
            outputStream.write(end);

        }
        catch (IOException e){
            //do nothing
        }
        byte c[] = outputStream.toByteArray( );

        Utils.writeContents(f, c); //yay

    }
}

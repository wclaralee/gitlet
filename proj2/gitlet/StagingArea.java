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

public class StagingArea implements Serializable {
    //class interacts with AddCommand

    private ArrayList<String> fileNames;
    private ArrayList<String> removedFiles; //not entirely sure if it goes here

    public StagingArea() {
        //INTIALIZATION
        this.fileNames = new ArrayList<String>();
        this.removedFiles = new ArrayList<String>();

    }
    public void clear() {
        this.fileNames = new ArrayList<String>();
        this.removedFiles = new ArrayList<String>();
    }
    public void unstage(String fileName) {
        this.fileNames.remove(fileName);
    }
    public boolean isEmpty() {
        if (this.fileNames == null || this.removedFiles == null) {
            //MIGHT HAVE SOME SILENT ERRORS, WHICH IS BAD
            return true; //return early
        }
        return ((this.fileNames.isEmpty() && this.removedFiles.isEmpty()));
    }
    public void stage(String fileName) {
        this.fileNames.add(fileName);
    }
    public boolean isInStaging(String fileName) {
        return fileNames.contains(fileName);
    }
    public boolean isInRemoved(String fileName) {
        return removedFiles.contains(fileName);
    }
    public void addRemovedFile(String fileName) {
        this.removedFiles.add(fileName);
    }
    public static StagingArea deserialize(String pathname) {
        File stagingArea = new File(pathname); //figure out g
        StagingArea returned = null;
        try {
            //deserialize the staging area
            ObjectInputStream input = new ObjectInputStream((new FileInputStream(stagingArea)));
            returned = (StagingArea) input.readObject();
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found in the deserialization of the staging area");
        } catch (Exception e) {
            System.out.println("IO exception in Staging Area deserialization");
        }
        return returned;
    }

    public void serialize(String pathName) {
        File stagingArea = new File(pathName);
        //delete these files if they already exist
        if (stagingArea.exists()) {
            stagingArea.delete();
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(stagingArea));
            out.writeObject(this);
            out.close();

        } catch (FileNotFoundException e) {
            System.out.println("change message later");
        } catch (IOException e) {
            System.out.println("change message later");
        }
        return;
    }

    public ArrayList<String> getRemovedFiles() {
        return this.removedFiles;
    }
    public ArrayList<String> getStagedFileNames() {
        return this.fileNames;
    }
}

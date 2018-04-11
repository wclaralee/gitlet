package gitlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;


public class Commit implements Serializable {
    private HashMap<String, String> blobNamesToHashes;
    private String parentHash;
    private String message;
    private Date commitDate;

    private String id;

    //create this commits id
    private String hashObjects(ArrayList<String> fileNames,
                               String messAge, Date date, String parentsHash) {
        ArrayList<Object> toHash = new ArrayList<Object>();
        for (int i = 0; i < fileNames.size(); i++) {
            //hash all contents
            byte[] bytes;
            try {
                bytes = Utils.readContents(new File(fileNames.get(i)));
                //also, while we're in this loop, might ass well add to the name hash
                this.blobNamesToHashes.put(fileNames.get(i), Utils.sha1(bytes));
            } catch (IllegalArgumentException e) {
                System.out.println(e);
                return null;
            }
            toHash.add(bytes);
        }
        toHash.add(date.toString());
        toHash.add(parentsHash);
        toHash.add(messAge);
        return Utils.sha1(toHash);
    }
    public Commit(Commit parent,  String message, ArrayList<String> fileNames) {
        if (parent == null) {
            //this is the first commit
            this.blobNamesToHashes = new HashMap<String, String>();
            this.commitDate = new Date();
            this.parentHash = ""; //no parent, so no parent hash
            this.message = message;
            this.id = this.hashObjects(fileNames, message, commitDate, "");
        } else {
            this.blobNamesToHashes = copyHash(parent.getFileMap()); //make a deep copy
            this.commitDate = new Date();
            this.parentHash = parent.getID();
            this.message = message;
            String[] inputs = new String[]{message};
            this.id = this.hashObjects(fileNames, message, commitDate, parentHash);
        }
    }
    //for deep copying hashmaps
    private HashMap<String, String> copyHash(HashMap<String, String> in) {
        HashMap<String, String> newMap = new HashMap<String, String>();
        for (String key : in.keySet()) {
            newMap.put(key, in.get(key));
        }
        return newMap;
    }
    //getters, setters
    public String getParentHash() {
        return this.parentHash;
    }
    public String getMessage() {
        return this.message;
    }
    public String getID() {
        return this.id;
    }
    public HashMap<String, String> getFileMap() {
        return blobNamesToHashes;
    }
    public String serialize(String pathname) {
        File f = new File(pathname);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(this);
            out.close();
        } catch (FileNotFoundException e) {
            //System.out.println("File not found for the commit serialization");
        } catch (IOException e) {
            //System.out.println("Found an error in the commit serialization");
        }
        return this.getID();
    }
    public static Commit deserialize(String fileName) {
        Commit returned = null;
        File inFile = new File(fileName);
        try {
            ObjectInputStream input = new ObjectInputStream((new FileInputStream(inFile)));
            returned = (Commit) input.readObject();
            input.close();
        } catch (FileNotFoundException e) {
            // System.out.println("File not found in the commit deserialization");

            //Version for checkout #2:
            //System.err.println("No commit with that id exists.");
        } catch (Exception e) {
            //System.out.println("IO excpetion for the commit deserialization");
        }
        return returned;
    }
    public Commit getLast() {
        String parentLocation = "./.gitlet/" + this.parentHash;
        Commit c = deserialize(parentLocation);
        return c;
    }
    @Override
    public String toString() {
        String rep = "";
        rep += " timeStamp = ";
        rep += this.commitDate.toString();
        rep += " parentHash = ";
        rep += parentHash;
        rep += " message=";
        rep += message;
        rep += " filenames=";
        for (String key : blobNamesToHashes.keySet()) {
            rep += key;
            rep += "|";
        }
        return rep;
    }
    public Date getCommitDate() {
        return this.commitDate;
    }
}

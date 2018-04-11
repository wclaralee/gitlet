package gitlet;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.FileInputStream;


public class Blob implements Serializable {
    private String hashId;
    private byte[] fileContents;


    public Blob(String filename) {
        try {
            //need to stream this as bytes
            fileContents = Utils.readContents(new File(filename));
        } catch (IllegalArgumentException j) {
            return;
        }
        hashId = Utils.sha1(fileContents);
    }

    public String getHashId() {
        return this.hashId;
    }
    public String hashMe() {
        return Utils.sha1(fileContents);
    }

    public byte[] getFileContents() {
        return fileContents;
    }

    public String serialize(String pathName) {
        File f = new File(pathName);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(this);
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found in the Blob serialization");
        } catch (IOException e) {
            System.out.println("Exception in the blobl serialization.");
        }
        return this.getHashId();
    }

    public static Blob deserialize(String pathName) {
        Blob returned = null;
        File inFile = new File(pathName);
        try {
            ObjectInputStream input = new ObjectInputStream((new FileInputStream(inFile)));
            returned = (Blob) input.readObject();
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found in Blob");
        } catch (Exception e) {
            System.out.println("Exception found in blobl deserialization");
        }
        return returned;
    }
}

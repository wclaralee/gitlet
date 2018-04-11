package gitlet;

public interface Command {
    void execute(); //overide this, might need to overload for other methods
    void validate(String s); //validate args

}

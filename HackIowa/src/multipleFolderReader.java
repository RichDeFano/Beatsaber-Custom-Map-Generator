import javax.swing.*;
import java.io.File;
import java.io.FileReader;

public class multipleFolderReader {
    public static void main(String args[])
    {
        //JFileChooser chooser = new JFileChooser();
        //chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //chooser.setDialogTitle("Please choose the appropriate directory");
        //chooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "CustomSongs"));
        //chooser.showOpenDialog(null);
        //File directory = chooser.getSelectedFile();
        //FileReader tempReader = new FileReader(jsonInput);
        File directory = new File(System.getProperty("user.dir") + "\\CustomSongs");
        System.out.println("Directory Accepted: " + directory.isDirectory());
        RecursiveFileReader.displayDirectoryContents(directory);
    }
}



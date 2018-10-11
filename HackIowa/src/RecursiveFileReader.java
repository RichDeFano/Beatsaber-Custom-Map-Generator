import java.io.File;
import java.io.IOException;

public class RecursiveFileReader {
    private static File songPath;
    private static File infoPath;
    private static File jsonPath;

    public static void displayDirectoryContents(File dir) {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    //System.out.println("directory:" + file.getCanonicalPath());
                    displayDirectoryContents(file);
                   // System.out.println("DEBUG: END OF DIRECTORY" + "|||" + songPath + "|||" + infoPath + "|||" + jsonPath );
                    folderToData nextFolder = new folderToData(songPath,jsonPath,infoPath);
                    nextFolder.calculateFromFolder();
                } else {

                    //System.out.println("     file:" + file.getCanonicalPath());
                    if (file.getName().toLowerCase().endsWith(".ogg"))
                    { songPath = file; }
                    if (file.getName().toLowerCase().endsWith(".json"))
                    {
                        if (file.getName().toLowerCase().equals("expert.json"))
                        {jsonPath = file;}
                        if (file.getName().toLowerCase().equals("info.json"))
                        {infoPath = file;}

                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileExtension(String fullName) {
        if (fullName != null)
        {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
        }
        else
        {
            String fileName = "";
            return fileName;
        }
    }

}
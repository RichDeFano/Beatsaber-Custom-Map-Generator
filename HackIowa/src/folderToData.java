import java.util.LinkedList;
import java.util.TreeMap;
import java.io.File;

import com.google.common.collect.ListMultimap;

public class folderToData {
    private File songLink;
    private File jsonLink;
    private File jsonInfo;

    public folderToData(File SongLink, File JsonLink, File JsonInfo)
    {
        songLink = SongLink;
        jsonLink = JsonLink;
        jsonInfo = JsonInfo;
    }
    public void calculateFromFolder()
    {
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println("---------------------------------------------------------------------------------");
        ///Converting the json and wav files to the time domain.
        wavToBytes inputtedSong = new wavToBytes(songLink);
        LinkedList<Integer> songToData = inputtedSong.wavToInt16();
        inputtedSong.initializeVariables();
        //inputtedSong.printVariables();
        int fileTime = inputtedSong.calculateSongTime();
        ImportJsonFiles inputtedFile = new ImportJsonFiles();

        //Printing out Bit Data
        System.out.println("Sorting Bits into time domain.. Please wait.");
        TreeMap<Double,Double> listOfBitsPer8th = inputtedSong.bitArrayToTimeDomainMap(songToData, fileTime);
        System.out.println("Sorting Complete.");

        ///Printing out json data
        System.out.println("Sorting json data into time domain.. Please wait.");
        ListMultimap<Double,LinkedList<Double>> listOfNotesPer8th = inputtedFile.jSonList(jsonLink,jsonInfo);
        System.out.println("Sorting Complete.");

        ///Data analysis
        System.out.println("Data ready to be clustered.");
        System.out.println("Note structure in respect to seconds." + listOfNotesPer8th);
        System.out.println("Average sound amplitude in respect to seconds." + listOfBitsPer8th);
        System.out.println("---------------------------------------------------------------------------------");
        //System.out.println("Thats all for this demo, Thanks!");
        System.out.println(" ");
        System.out.println(" ");

    }

}

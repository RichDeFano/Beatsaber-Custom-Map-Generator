import java.util.LinkedList;
import java.util.TreeMap;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;


public class wavToBeatsaberMain {
    public static void main(String args[]) {
        wavToBytes inputtedSong = new wavToBytes();
        LinkedList<Integer> songToData = inputtedSong.wavToInt16();
        inputtedSong.initializeVariables();
        //double numbOfBits = songToData.size();
        //System.out.println("# of Bits: " + numbOfBits);
        int fileTime = inputtedSong.calculateSongTime();
        //System.out.println(fileTime);
        ImportJsonFiles inputtedFile = new ImportJsonFiles();
        System.out.println("Sorting json data into time domain.. Please wait.");
        ListMultimap<Double,LinkedList<Double>> listOfNotesPer8th = inputtedFile.jSonList();
        System.out.println("Sorting Complete.");

        System.out.println("Sorting Bits into time domain.. Please wait.");
        TreeMap<Double,Double> listOfBitsPer8th = inputtedSong.bitArrayToTimeDomainMap(songToData, fileTime);
        System.out.println("Sorting Complete.");
        System.out.println("Data ready to be clustered.");
        System.out.println("Thats all for this demo, Thanks!");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("DEBUG: Notes in respect to seconds." + listOfNotesPer8th);
        System.out.println("DEBUG: Average sound amplitude in respect to seconds." + listOfBitsPer8th);
        //System.out.println("DEBUG: Size of bits per 8th second: " + listOfBitsPer8th.size());
        //System.out.println("DEBUG: Size of note vectors per 8th second: " + listOfNotesPer8th.size());
    }

}

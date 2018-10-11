import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.ArrayListMultimap;

public class ImportJsonFiles {
    private String songName;
    //private double bpm;
    private String songSubName;
    private String authorName;
    private double beatsPerMinute;
    private double previewStartTime;
    private double previewDuration;
    private String coverImagePath;
    private String environmentName;
    private String version;
    private double beatsPerBar;
    private double noteJumpSpeed;
    private double shuffle;
    private double shufflePeriod;
    private LinkedList<LinkedList<Double>> notesList = new LinkedList<>();

    public ListMultimap<Double,LinkedList<Double>> jSonList(File tempJson,File tempInfo) {
        JSONParser parser = new JSONParser();
        ImportJsonFiles json = new ImportJsonFiles();
        try {
            ////
            //JFileChooser chooser = new JFileChooser();
            //chooser.setDialogTitle("Please choose the appropriate .json file:");
            //chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            //chooser.showOpenDialog(null);
            //File jsonInput = chooser.getSelectedFile();
            //FileReader tempReader = new FileReader(jsonInput);
            ///
            FileReader tempReader = new FileReader(tempJson);


            JSONObject jsonFile;
            try
            {jsonFile = (JSONObject) parser.parse(tempReader);}
            catch (Exception IOException)
            {
                jsonFile = null;
                System.out.println("Error: json File not initialized");
            }
            version = (String) jsonFile.get("_version");
            beatsPerBar = ((Number)jsonFile.get("_beatsPerBar")).doubleValue();
            noteJumpSpeed = ((Number)jsonFile.get("_noteJumpSpeed")).doubleValue();
            shuffle = ((Number)jsonFile.get("_shuffle")).doubleValue();
            shufflePeriod = ((Number)jsonFile.get("_shufflePeriod")).doubleValue();
            jSonInfo(tempInfo);
            JSONArray notes = (JSONArray) jsonFile.get("_notes");


            Iterator<JSONObject> notesIt = notes.iterator();
            while (notesIt.hasNext()) {
                JSONObject noteObject = (JSONObject) notesIt.next();
                Iterator<JSONObject> listOf5Itr = noteObject.entrySet().iterator();
                LinkedList<Double> eachFive = new LinkedList<>();
                while (listOf5Itr.hasNext())
                {
                    String[] tempArray = new String[5];
                    LinkedList<Double> doubArray = new LinkedList<>();
                    Map.Entry pair = (Map.Entry) listOf5Itr.next();
                    //System.out.println(pair);
                    if (pair.getKey().equals("_time")) {
                        tempArray[0] = String.valueOf(pair.getValue());
                        String tempValue = tempArray[0];
                        if (tempValue != null && !tempValue.isEmpty()) {
                            double tempDouble = Double.parseDouble(tempValue);
                            ///this "time" isnt in seconds, but in ascending beats. We need to oonvert it to seconds. secondsIn = ((_time)/(BPM/60))
                            //System.out.println("DEBUG: " + beatsPerMinute);
                            double secondsIn = ((tempDouble)/(beatsPerMinute/60));
                            //System.out.println("Temp: secondsIn =" + secondsIn + "||" + tempDouble + "," + bpm + "," + (bpm/60));
                            eachFive.add(secondsIn);
                        } else {
                            eachFive.add(0.0);
                        }
                        //System.out.println("time: " + tempArray[0]);
                    }
                    else if (pair.getKey().equals("_lineIndex")) {
                        tempArray[1] = String.valueOf(pair.getValue());
                        String tempValue = tempArray[1];
                        if (tempValue != null && !tempValue.isEmpty()) {
                            double tempDouble = Double.parseDouble(tempValue);
                            //System.out.println("temp Double: " + tempDouble);
                            eachFive.add(tempDouble);
                        } else {
                            eachFive.add(0.0);
                        }
                        //System.out.println("lI: " + tempArray[1]);
                    }
                    else if (pair.getKey().equals("_lineLayer")) {
                        tempArray[2] = (String.valueOf(pair.getValue()));
                        String tempValue = tempArray[2];
                        if (tempValue != null && !tempValue.isEmpty()) {
                            double tempDouble = Double.parseDouble(tempValue);
                            //System.out.println("temp Double: " + tempDouble);
                            eachFive.add(tempDouble);
                        } else {
                            eachFive.add(0.0);
                        }
                        //System.out.println("lL: " + tempArray[2]);
                    }

                    else if (pair.getKey().equals("_type")) {
                        tempArray[3] = String.valueOf(pair.getValue());
                        String tempValue = tempArray[3];
                        if (tempValue != null && !tempValue.isEmpty()) {
                            double tempDouble = Double.parseDouble(tempValue);
                            //System.out.println("temp Double: " + tempDouble);
                            eachFive.add(tempDouble);
                        } else {
                            doubArray.add(0.0);
                        }
                        //System.out.println("type: " + tempArray[3]);
                    }
                    else if (pair.getKey().equals("_cutDirection")) {
                        tempArray[4] = String.valueOf(pair.getValue());
                        String tempValue = tempArray[4];
                        if (tempValue != null && !tempValue.isEmpty()) {
                            double tempDouble = Double.parseDouble(tempValue);
                            //System.out.println("temp Double: " + tempDouble);
                            eachFive.add(tempDouble);
                        } else {
                            eachFive.add(0.0);
                        }
                        //System.out.println("cD: " + tempArray[4]);
                    }
                }
                json.notesList.add(eachFive);
            }
            //System.out.println("notesList:" + json.notesList);
        }
        catch (FileNotFoundException e){
        System.out.println("Error: FileNotFoundException.");
        }
        //System.out.println("DEBUG: " + json.notesList);
        //uSystem.out.println("DEBUG: noteList Size = " + json.notesList.size());
        ListMultimap<Double,LinkedList<Double>> tempMap = jSonToTimeDomainMap(json.notesList);
        //System.out.println("DEBUG: " + tempMap);
        return (tempMap);
    }

    public void jSonInfo(File tempInfo) {

            JSONParser parser = new JSONParser();
            ImportJsonFiles json = new ImportJsonFiles();
            try {
                ////
                //JFileChooser chooser = new JFileChooser();
                //chooser.setDialogTitle("Please choose the appropriate .json info file:");
                //chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                //chooser.showOpenDialog(null);
                //File jsonInput = chooser.getSelectedFile();
                FileReader tempReader = new FileReader(tempInfo);
                ///
                JSONObject jsonFile;
                try {
                    jsonFile = (JSONObject) parser.parse(tempReader);
                } catch (Exception IOException) {
                    jsonFile = null;
                    System.out.println("Error: json File not initialized");
                    }
                songName = (String) jsonFile.get("songName");
                songSubName = (String) jsonFile.get("songSubName");
                authorName = (String) jsonFile.get("authorName");
                beatsPerMinute = ((Number)jsonFile.get("beatsPerMinute")).doubleValue();
                previewStartTime = ((Number)jsonFile.get("previewStartTime")).doubleValue();
                previewDuration = ((Number)jsonFile.get("previewDuration")).doubleValue();
                coverImagePath = (String) jsonFile.get("coverImagePath");
                environmentName = (String) jsonFile.get("environmentName");
                printInfo();
                JSONArray difficultyArray = (JSONArray) jsonFile.get("difficultyLevels");

/*
    private String songName;
    //private double bpm;
    private String songSubName;
    private String authorName;
    private double beatsPerMinute;
    private double previewStartTime;
    private double previewDuration;
    private String coverImagePath;
    private String environmentName;
 */
            }
            catch (FileNotFoundException e){
                System.out.println("Error: FileNotFoundException.");
            }
        }

    public void printInfo()
    {
     System.out.println("******************SONG INFORMATION******************");
     System.out.println("Song Name: " + songName);
     System.out.println("Artist Name: " + songSubName);
     System.out.println("Beatmap Author: " + authorName);
     System.out.println("Beats Per Minute: " + beatsPerMinute);
     System.out.println("Preview Start Time: " + previewStartTime + " seconds.");
     System.out.println("Preview Duration: " + previewDuration + " seconds.");
     System.out.println("Cover Image FilePath: " + System.getProperty("user.dir") + coverImagePath );
     System.out.println("Environment Name: " + environmentName);
     System.out.println("Version: " + version);
     System.out.println("Beats Per Bar: " + beatsPerBar);
     System.out.println("Note Jump Speed: " + noteJumpSpeed);
     System.out.println("Shuffle: " + shuffle);
     System.out.println("Shuffle Period: " + shufflePeriod);
     System.out.println("****************************************************");

 }
    public ListMultimap<Double,LinkedList<Double>> jSonToTimeDomainMap(LinkedList<LinkedList<Double>> notesList)
    {
        ListMultimap<Double,LinkedList<Double>> multimap = ArrayListMultimap.create();
        //TreeMap<Double,LinkedList<Double>> fullJSonMap = new TreeMap<>();
        Iterator fullDataIterator = notesList.iterator();
        while (fullDataIterator.hasNext()) {
            LinkedList<Double> at5 = (LinkedList<Double>)fullDataIterator.next();
            double timeAt = at5.remove(3);
            //System.out.println("DEBUG: " + timeAt);
            multimap.put(timeAt,at5);
            }

       return multimap;
    }


}

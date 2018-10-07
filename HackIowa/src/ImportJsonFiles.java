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
    private double bpm;
    private LinkedList<LinkedList<Double>> notesList = new LinkedList<>();

    public ListMultimap<Double,LinkedList<Double>> jSonList() {
        JSONParser parser = new JSONParser();
        ImportJsonFiles json = new ImportJsonFiles();
        try {
            ////
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Please choose the appropriate .json file:");
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            chooser.showOpenDialog(null);
            File jsonInput = chooser.getSelectedFile();
            FileReader tempReader = new FileReader(jsonInput);
            ///
            JSONObject jsonFile;
            try
            {jsonFile = (JSONObject) parser.parse(tempReader);}
            catch (Exception IOException)
            {
                jsonFile = null;
                System.out.println("ErrorL json File not initialized");
            }
            json.songName = (String) jsonFile.get("_name");
            Long tempLong = (long) jsonFile.get("_beatsPerMinute");
            json.bpm = tempLong.doubleValue();
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
                            double secondsIn = ((tempDouble)/(json.bpm/60));
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
        //uSystem.out.println("DEBUG: noteList Size = " + json.notesList.size());
        ListMultimap<Double,LinkedList<Double>> tempMap = jSonToTimeDomainMap(json.notesList);
        return (tempMap);
    }

    public ListMultimap<Double,LinkedList<Double>> jSonToTimeDomainMap(LinkedList<LinkedList<Double>> notesList)
    {
        ListMultimap<Double,LinkedList<Double>> multimap = ArrayListMultimap.create();
        //TreeMap<Double,LinkedList<Double>> fullJSonMap = new TreeMap<>();
        Iterator fullDataIterator = notesList.iterator();
        while (fullDataIterator.hasNext()) {
            LinkedList<Double> at5 = (LinkedList<Double>)fullDataIterator.next();
            double timeAt = at5.remove(3);
            multimap.put(timeAt,at5);
            }

       return multimap;
    }


}

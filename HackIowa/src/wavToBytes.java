import java.io.ByteArrayOutputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import java.io.File;
import javax.swing.JFileChooser;
import java.util.LinkedList;
import java.text.DecimalFormat;
import java.util.TreeMap;

public class wavToBytes {
    private static  AudioInputStream songBeingRead;
    private boolean bigEndian;
    private int channels;
    private AudioFormat.Encoding encoding;
    private float frameRate;
    private int frameSize;
    private float sampleRate;
    private int sampleSizeInBits;
    private static int fileLengthInBytes;
    private float bitRate;


    public wavToBytes()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Please choose the appropriate .wav file:");
        chooser.setCurrentDirectory(new File (System.getProperty("user.dir")));
        chooser.showOpenDialog(null);
        File soundFile = chooser.getSelectedFile();
        try
            {songBeingRead = AudioSystem.getAudioInputStream(soundFile);}
        catch (Exception UnsupportedAudioFileException)
            {System.out.println("Unsupported Sound File Type.");}
    }
    float getFrameRate()
    {return frameRate;}
    public void initializeVariables()
    {
        AudioFormat headerData = songBeingRead.getFormat();
        bigEndian = headerData.isBigEndian();
        channels = headerData.getChannels();
        encoding = headerData.getEncoding();
        frameRate = headerData.getFrameRate();
        frameSize = headerData.getFrameSize();
        sampleRate = headerData.getSampleRate();
        sampleSizeInBits = headerData.getSampleSizeInBits();
        bitRate = (sampleRate*sampleSizeInBits*channels); ////(sample_rate) * (bit_depth) * (number_of_channels)
    }
    public void printVariables()
    {System.out.println(frameRate + " " + frameSize);}
    public int calculateSongTime()
    {
        float finalTime;
        finalTime = ((fileLengthInBytes)/8);
        finalTime = (finalTime/bitRate);
        DecimalFormat df = new DecimalFormat("00.00");
        String tempTime = df.format(finalTime);

        String[] parts = tempTime.split("\\.");
        int minToSec = Integer.parseInt(parts[0])*60 ;
        int restOfSec = Integer.parseInt(parts[1]);
        int finalTimeInSec = minToSec+restOfSec;
        //tempTime = tempTime.replace(".",":");
        return finalTimeInSec;
    }
    public LinkedList<Integer> wavToInt16()
    {
        LinkedList<Integer> bitList = new LinkedList<>();
            ///Reading in a WAV file and converting the waveform of the audio to an array of Bytes, which represent the amplitude.
            ByteArrayOutputStream songByteOS = new ByteArrayOutputStream();
            try
            {
            songBeingRead.transferTo(songByteOS);
            byte[] songByteArray = songByteOS.toByteArray();
            fileLengthInBytes = songByteArray.length;
            ///Adding two successive bytes together, to create one 16 Bit Signed little-E PCM Number.
                for (int j = 0; j+1<songByteArray.length;j=j+2)
                {
                    int bytesToInt16 = (byte)(songByteArray[j]+songByteArray[j+1]);
                    bitList.add(bytesToInt16);
                }
            }
            catch (Exception IOException)
            {System.out.println("Error: IO Exception."); }
            //ystem.out.println("Bitlist: " + bitList);
            //System.out.println("Number of Bits: " + bitList.size());
        return bitList;
    }

    public TreeMap<Double,Double> bitArrayToTimeDomainMap(LinkedList<Integer> songToData, int fileTime) {
        TreeMap<Double,Double> listOfBitsPer8th = new TreeMap<>();
        float tempFloat = this.getFrameRate();
        for (int i = 0; i < (fileTime * 8); i++) {
            double Hz = Double.parseDouble(new Float(tempFloat).toString());
            //System.out.println(Hz);
            //LinkedList<Double> bitsPer8th = new LinkedList<>();
            double avOfBits = 0;
            boolean exitLoop = false;
            while (!exitLoop) {
                for (double j = 0; j < (Hz / 8); j++) {
                    double tempBit = songToData.remove(i);
                    avOfBits = avOfBits + tempBit;
                    //bitsPer8th.add(tempBit);
                }
                double fullAverage = (avOfBits / (Hz / 8));
                double counter = i;
                if (counter != 0)
                {listOfBitsPer8th.put(counter/8,fullAverage);}
                else
                {listOfBitsPer8th.put(counter,fullAverage);}
                exitLoop = true;
            }
            //listOfBitsPer8th.put(i,fullAverage);

        }
        return listOfBitsPer8th;
    }

}

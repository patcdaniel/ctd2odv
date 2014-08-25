/**
 * Created by Patrick on 8/8/14.
 *
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadText {

    private String[] fileNames;
    private double userLatitude;
    private double userLongitude;
    private String castDate; //"mon/day/yr"
    private String castTime; //"hh:mm" military time
    private ArrayList<String> header = new ArrayList<String>();
    private int headerLength = 0;
    private String type = "C";
    private String cruiseName;
    private String stationName;
    private File file;
    private File outFile;
    private Boolean append = false;
    private FileWriter writer;

    public ReadText(String[] fileName){
        this.fileNames = fileName;
    }

    private void writeToText(String stringData){
        if (this.append == false) {
            outFile = new File(this.cruiseName + ".txt");
            try {
                this.outFile.createNewFile();
                this.writer = new FileWriter(this.outFile, append);
                this.append = true;
                this.writer.write("\"Cruise\", \"Station\", \"Type\", \"mon/day/yr\", \"hh:mm\", \"Longitude (degrees_east)\", \"Latitude (degrees_north)\", ");
                for (int i = 0; i < this.header.size(); i++) {
                    this.writer.write(" \"" + header.get(i) + "\",");
                }
                this.writer.write("\n");
            } catch (Exception ex) {
                System.out.println("Could write to file. Here's why:");
                ex.printStackTrace();
            }
        }
        try{
            this.writer.write(this.cruiseName + ", " + this.stationName + ", C, " + this.castDate + ", " + this.castTime + ", " + this.userLongitude + ", " + this.userLatitude + ", " + stringData + "\n");
            this.writer.flush();
        } catch (Exception ex1) {
            System.out.println("Could write to file. Here's why:");
            ex1.printStackTrace();
        }
    }

    public void loadFile() {
        this.file = new File(this.fileNames[0]);
        try {
            readFile(file);
        } catch (Exception ex) {
            System.out.println("That didn't work. Here's why:");
            ex.printStackTrace();
        }
    }

    private void readFile(File file) throws IOException, FileNotFoundException{
        boolean dataFound = false;
        String nextLine = "";
        int lineIndex = 0;
        String[] dataList;
        String filename = file.getName();
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(file.getAbsoluteFile())));
        scanner.useDelimiter("\n"); //Treat newLine character as the delimited, returning single line tokens
        Scanner keyboard = new Scanner(System.in);
        enterCruiseName(keyboard);
        while(scanner.hasNext()) {
            nextLine = scanner.nextLine();
            if (dataFound == false) {
                if (nextLine.startsWith("#")) {
                    containsTimeStamp(nextLine);
                    containsName(nextLine);
                } else if (nextLine.startsWith("* ")) {
                    containsStationName(nextLine);
                } else if (nextLine.startsWith("**")) {
                    enterLatLon(keyboard, scanner, nextLine);
                } else if (nextLine.startsWith("*END*")) {
                    dataFound = true;
                }

            }
            else {
                nextLine = nextLine.trim();
                nextLine = nextLine.replaceAll("\\s+", ",");
                writeToText(nextLine);
            }
        }
    }


    private void formatString(String[] strData) {
        String outString = "";
        for (int i = 0; i < strData.length; i++) {
            outString = outString + ", " + strData[i];
        }
        System.out.println(outString);
    }

    private void enterLatLon(Scanner keyboard, Scanner scanner, String nextLine) {
        String prnStr = "";
        while (nextLine.startsWith("**")) {
            prnStr = prnStr + nextLine + "\n";
            nextLine = scanner.nextLine();
        }
        System.out.print(prnStr);
        System.out.println("Please enter the Latitude: ");
        this.userLatitude = keyboard.nextDouble();
        System.out.println("Please enter the Longitude: ");
        this.userLongitude = keyboard.nextDouble();
        if (userLongitude > 0d) {
            String answer = "";
            do {
                System.out.println("Should that be -" + this.userLongitude + "?");
                answer = keyboard.next();
            } while (!(answer.toLowerCase().equals("no")) && !(answer.toLowerCase().equals("yes")));
            if (answer.toLowerCase().equals("yes")) {
                this.userLongitude = this.userLongitude*-1;
            }
            System.out.println(userLatitude);
            System.out.println(userLongitude);
        }
    }

    private void enterCruiseName(Scanner keyboard) {
        System.out.println("Please enter a cruise name: ");
        this.cruiseName = keyboard.next();
    }

    private void containsStationName (String string) {
        Pattern pattern = Pattern.compile("\\* (cast  \\d+)");
        Matcher matcher = pattern.matcher(string);
        boolean found = false;
        while (matcher.find()) {
            this.stationName = matcher.group(1);
            System.out.println(stationName);
        }
    }

    private void containsName (String string) {
        Pattern pattern = Pattern.compile("\\#.name \\d.+: (.+)");
        Matcher matcher = pattern.matcher(string);
        boolean found = false;
        while (matcher.find()) {
            this.header.add(matcher.group(1));
            this.headerLength++;
        }
    }

    private void containsTimeStamp(String string) {
        Pattern pattern = Pattern.compile("\\# start_time = (\\w.+) \\[.+");
        Matcher matcher = pattern.matcher(string);
        boolean found = false;
        while (matcher.find()) {
            String strDate = matcher.group(1);
            String[] parts = strDate.split(" ");
            this.castDate = convertMonth(parts[0]) + "/" + parts[1]+ "/" + parts[2];
            this.castTime = parts[3].substring(0,5);
            System.out.println(castDate);
            System.out.println(castTime);
        }
    }

    private String convertMonth(String monStr) {

        String[] month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for (int i = 0; i < month.length; i++) {
            if (monStr.equalsIgnoreCase(month[i])) {
                i = i+1;
                if (i < 10) {
                    String strI = "0" + i;
                    return strI;
                }
                else {
                    String strI = "" + i;
                    return strI;
                }
            }
        }
        String strI = "-1";
        return strI;
    }

}

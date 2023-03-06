
package sensorapplication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SensorAppLication {

    public static void main(String[] args)  {

        // --- Client information ---
        int port = 7541; // Initilize port number for the client.
        String host = "localhost"; // Initilize host name for the client.

        // Print a header
        System.out.println("Sensor Online. (Sensor Application class)");
         
        // Excution time
        int excutionTime = 60;

        // Start the Connection; Persistent TCP Connection
        long t0 = System.currentTimeMillis();

        // -- START TIMER --
        Timer timer = new Timer();
        TimerTask task;
        task = new TimerTask() {
            
            Socket clientSocket = null;
             BufferedWriter writer =null;
              
              // run() method to carry out the action of the task
              public void run() {
                  

                      if (System.currentTimeMillis() - t0 > 1000 * excutionTime) {
                          // Stop timer
                          cancel();
                          System.out.println("Connection Is Ended");
                          try {
                              clientSocket.close();
                              writer.close();
                              System.exit(0);
                          } catch (IOException e) {}
                          
                          
                      } // End of if
                      
                      // -- Extract Data --
                      String GeneratedNumbers = GenerateNumbers();
                      String [] data = GeneratedNumbers.split("-");
                      
                      try {
                      clientSocket = new Socket(host, port); // Initilize Socket for the client.
                      writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                      writer.write(GeneratedNumbers);
                      writer.flush();
                    
                  } catch (Exception e) {}
                  System.out.println();
                };
          };

        // TIMER SCHEDULER START FROM 0 AND DO TASK EVERY 5 SEC
        timer.schedule(task, 0, 5000);
    }
      
       public static String GenerateNumbers() {
        StringBuilder sensorData = new StringBuilder(); // Sensor data as StringBuilder

        // Temp rane is from 36-41C
        double temp1 = (double) (Math.random() * (41 - 36 + 1) + 36);
        double temp = (int) (Math.round(temp1 * 10)) / 10.0;
        sensorData.append(temp + "-"); // Append to the sensor data

        // Heart Rate range is from 50-120bpm
        int heartRate = (int) (Math.random() * (120 - 50 + 1) + 50);
        sensorData.append(heartRate + "-"); // Append to the sensor data

        // Oxygen range is from 60-120%
        int oxygen = (int) (Math.random() * (120 - 60 + 1) + 60);
        sensorData.append(oxygen + "-"); // Append to the sensor data

        // -- MSG ARRANGING --
        String localDate = String.format("%1$te %1$tb %1$ty", new Date()); // Get date
        sensorData.append(localDate + "-"); // Append to the sensor data

        String localTime = getFormattedDate(); // Get time
        sensorData.append(localTime + "-"); // Append to the sensor data

        // -- SENSOR OUTPUT --
        System.out.println( // Temp Outpt
                "At date: " + localDate + " time: " + localTime + ", sensed temperature is " + temp);

        System.out.println( // Heart Rate Output
                "At date: " + localDate + ", time: " + localTime + ", sensed heart rate is " + heartRate);

        System.out.println( // Oxygen Output
                "At date: " + localDate + ", time: " + localTime + ", sensed oxygen saturation is " + oxygen);

        System.out.println();
        return sensorData.toString();
    }

    public static String getFormattedDate() {
        LocalTime myDateObj = LocalTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }
    
    
}

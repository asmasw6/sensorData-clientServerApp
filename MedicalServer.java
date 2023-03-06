
package sensorapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class MedicalServer {
    static ServerSocket serverSocket = null;
    public static void main(String[] args) throws IOException {
       
         //-- Server information --
        int port =7542;
        serverSocket = new ServerSocket(7542);
        // -- create complete server socket
        //start connection
        while(true){
            
            Socket connectedSocket = serverSocket.accept();
            System.out.println("the mdical server is conected");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connectedSocket.getInputStream()));
            String personalServerMSG = "";
            while (true) {
                String data = reader.readLine();
                
                if (data != null) {
                    // read messages
                     personalServerMSG = data;
                    while ((data = reader.readLine()) != null) {
                        if (data.isEmpty()) {
                            break;
                        }
                        personalServerMSG += "\n" + data;
                    }
                    
                
                //raed all lines from client
                double temp = Double.parseDouble(reader.readLine());
                int hr = Integer.parseInt(reader.readLine());
                int oxy = Integer.parseInt(reader.readLine());
                               
                //-- output --
                System.out.println(personalServerMSG);
                // emergency level
                int emergencyLevel;
               
                if(temp > 39 && hr > 100 && oxy < 95){
                    System.out.println("ACTION: Send an ambulance to the patient!"); // Output
                        emergencyLevel = 1;
                }else if(38 < temp && temp < 38.9 && 95 < hr &&hr < 98 && oxy < 80){
                    System.out.println("ACTION: Call the patient's family!"); // Output
                        emergencyLevel = 2;
                }else{
                System.out.println("ACTION: Warning, advise patient to make a check up appointment!"); // Output
                        emergencyLevel = 3;
                }
                
                System.out.println();
                if(emergencyLevel >=1 && emergencyLevel<=3){
                    if(emergencyLevel == 1 ){
                        System.out.println("SEND THE AMBULANCE!");
                 }else if(emergencyLevel == 2 ){
                        System.out.println("CALL PATIENT'S FAMILY!");
                  }else if(emergencyLevel == 3){
                        System.out.println("ADVICE PATIENT TO CHECK!");
                  }
                 }
                      break;
                 }
            }
            System.exit(0);
        }
    }
    
}

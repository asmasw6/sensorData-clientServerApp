
package sensorapplication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class PersonalServer extends Thread {
        private final static String HOST = "localhost"; // Server host, localhost should be fine anytime.
        private final static int PORT = 7541; // Server port
        private String sensorData;

        public PersonalServer(String sensorData) {
                this.sensorData = sensorData;
        }
        public static String[] ExtractSensorData(String sensorData) {
                return sensorData.split("-");
        }

        @Override
        public void run(){
            //-- client info -- 
            int port = 7542;
            Socket clientSocket = null;
            BufferedWriter writer = null;
            
                // -- Create Complete Client Socket --
                try {
                        // Create Client Socket
                        clientSocket = new Socket(HOST, port);
                        // Create Buffer Writer
                        writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                } catch (Exception e) {
                }
            
                      
            while(true){
                // Send SENSOR DATA to Personal Server (Server Side)
                     try {
                                writer.write(sensorData);
                                writer.flush();
                       } catch (IOException e) {}
                     
                        System.out.println();
                        
                        try {
                            clientSocket.close();
                            writer.close();
                            break;     
                       } catch (IOException e) {}
            }
            System.exit(0);
    };
          
        
        public static void main(String[] args) throws IOException{

		// --- Server information ---                 
                 ServerSocket serverSocket = new ServerSocket(PORT);
      		// Start the Connection; Persistent TCP Connection
		while (true) {
			// Establish a connection by using socket object for the serverSocket
			Socket connectedSocket = serverSocket.accept();
                          Scanner fromClient = new Scanner(connectedSocket.getInputStream());                        
                         while (fromClient.hasNextLine()) {

                                            String sensorData = fromClient.nextLine(); // Read Sensor Data (Client)
                                            //extract data
                                            String[] sensorArray = ExtractSensorData(sensorData);
					double temperature = Double.parseDouble(sensorArray[0]);
					int heartRate = Integer.parseInt(sensorArray[1]);
					int oxygen = Integer.parseInt(sensorArray[2]);
					String date = sensorArray[3];
					String time = sensorArray[4];

					boolean notify = false; // NOTIFICATION SIGN
					StringBuilder message = new StringBuilder();
					String dateNtime_msg = "At date: " + date + ", time: " + time;

					// -- Personal Server Output --
					System.out.print(dateNtime_msg);
                                            
                                            // -- Personal Server Cases --
                                            // -- Temp --
					if (temperature > 38) {
						message.append(dateNtime_msg);
						String msg = ",Temperature is high " + temperature;
						System.out.println(msg + ". An alert message is sent to the Medical Server");
						notify = true; // NOTIFY MEDICAL SERVER SIGN
						message.append(msg + "\n");

					} else {
						System.out.println(" ,Temperature is normal");
					}

					// ---- HEART RATE ----
					System.out.print(dateNtime_msg);
					if (heartRate > 100) {
						message.append(dateNtime_msg);
						String msg = " , Heart rate is above normal " + heartRate;
						System.out.println(msg + ". An alert message is sent to the Medical Server");
						message.append(msg + "\n");
						notify = true; // NOTIFY MEDICAL SERVER SIGN

					} else if (heartRate < 60) {
						message.append(dateNtime_msg);
						String msg = ", Heart rate is high " + heartRate;
						System.out.println(msg + ". An alert message is sent to the Medical Server");
						message.append(msg + "\n");
						notify = true; // NOTIFY MEDICAL SERVER SIGN

					} else {
						System.out.println(", Heart rate is normal");
					}

					// ---- OXYGEN ----
					System.out.print(dateNtime_msg);
					if (oxygen < 75) {
						message.append(dateNtime_msg);
						String msg = ", Oxygen saturation is low " + oxygen;
						System.out.println(msg + ". An alert message is sent to the Medical Server");
						message.append(msg + "\n");
						notify = true; // NOTIFY MEDICAL SERVER SIGN

					} else {
						System.out.println(", Oxygen is normal");
					}
                                        
					// -- NOTIFY MEDICAL SERVER ---
                                             String data = temperature + "\n" + heartRate + "\n" + oxygen;
					if (notify) {
						// collect all message of the medical server
						PersonalServer medicalServerNotification = new PersonalServer(
								message.toString() + "\n" + data);
						medicalServerNotification.start();
					}
                                        System.out.println();

			} //Entire while loop
                        
                 }//Outer while loop
        }     
}


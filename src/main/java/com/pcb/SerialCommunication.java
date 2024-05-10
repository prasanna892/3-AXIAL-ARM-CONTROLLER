package com.pcb;


import java.io.*;
import java.util.Scanner;

import com.fazecast.jSerialComm.SerialPort;

public class SerialCommunication {
    public static void main(String[] args) throws IOException, InterruptedException
    {
        SerialPort conn = SerialPort.getCommPort("COM4");
        conn.setComPortParameters(9600, 8, 1, 0);
        conn.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
        //-------------------------------------------------------------
        if(!conn.openPort()) {
            System.out.println("\nCOM port NOT available\n"); return;
        }
        //-------------------------------------------------------------
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.print("\nEnter number to control motor (0 to stop and 9 to exit): ");
            String blinks = "code " + input.nextInt(); 
            if (blinks.charAt(blinks.length() - 1) == '9') {
                conn.getOutputStream().write("0\n".getBytes());
                break;
            }
            Thread.sleep(1500);
            conn.getOutputStream().write((blinks + "\n").getBytes());
            if (conn.getInputStream().available() > 0) {
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                try {
                    // Read data
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                    // Close the reader
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        input.close();
        conn.closePort();
    }
}
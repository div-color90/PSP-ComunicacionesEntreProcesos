package org.example;

import java.io.*;

public class Tuberia {
    public static void main(String[] args) {

        try {
            ProcessBuilder primerProceso = new ProcessBuilder("CMD", "/C", "DIR");
            Process proceso1 = primerProceso.start();
            ProcessBuilder segundoProceso = new ProcessBuilder("FIND", "\".gitignore\"");
            Process proceso2 = segundoProceso.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(proceso1.getInputStream()));

            String linea;

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(proceso2.getOutputStream()));
            while ((linea = br.readLine()) != null) {
                bw.write(linea);
                bw.newLine();
            }

            br.close();
            bw.close();

            BufferedReader br2 = new BufferedReader(new InputStreamReader(proceso2.getInputStream()));
            String linea2;
            while ((linea2 = br2.readLine()) != null) {
                System.out.println(linea2);
            }
            br2.close();

           proceso2.waitFor();
            System.out.println("Finalizada la ejecución ");


        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

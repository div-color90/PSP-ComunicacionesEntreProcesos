package org.example;

import org.iesalandalus.programacion.utilidades.Entrada;

import java.io.*;

public class Lector {
    public static void main(String[] args) {

        try {
            ProcessBuilder proceso = new ProcessBuilder("java", "org.example.Escritor");
            proceso.directory(new File("C:\\Users\\ajco1\\OneDrive\\Documentos\\2DAM\\ProgramaciónProcesos\\ComunicaciónProcesos\\build\\classes\\java\\main"));
            proceso.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process procesoEscritor = proceso.start();
            System.out.print("Introduzca un número: ");
            int numero = Entrada.entero();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(procesoEscritor.getOutputStream()));
            bw.write(String.valueOf(numero));
            bw.newLine();
            bw.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(procesoEscritor.getInputStream()));
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
            br.close();

            procesoEscritor.waitFor();


        } catch (InterruptedException inter) {
                System.out.println(inter.getMessage());;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }
}
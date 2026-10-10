package org.example;

import org.iesalandalus.programacion.utilidades.Entrada;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Escritor {

    public static void main(String[] args){

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int numero = Integer.parseInt(br.readLine());

            for (int i = 1; i <= 10; i++){
                System.out.println(numero + i);
            }
            br.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }


    }

}

package com.example.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MyClass {

    public static void main(String[] args) {

      String inputFile = "example.txt";
      String outputFile = "result.txt";

        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine())!=null){
                System.out.println("Start"+line);
                line = line.replace("0","10")
                        .replace("1","11")
                        .replace("2","12")
                        .replace("3","13")
                        .replace("7","17");
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
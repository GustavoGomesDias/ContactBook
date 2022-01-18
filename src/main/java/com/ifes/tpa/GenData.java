/*
 * Gerado de Agendas
 * Autor: Mateus Costa
 * Editado por: Gustavo Gomes Dias
 */

package com.ifes.tpa;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

import com.github.javafaker.Faker;

public class GenData {
    public static void teste1() {
        Faker faker = new Faker();
        BufferedWriter buffwrite;
        int i=0;
        String name, nomearq;
        String firstName;
        String lastName;
        String city;
        String phone, registro;
        Scanner leitor = new Scanner(System.in);

        System.out.print("Nome do Arquivo de saida: ");
        nomearq = leitor.next();
        try {
            buffwrite = new BufferedWriter(new FileWriter(nomearq));

            while(i<10000) {
                name = faker.name().fullName();
                firstName = faker.name().firstName();
                lastName = faker.name().lastName();
                city = faker.address().city();
                phone = faker.phoneNumber().cellPhone();
                registro= firstName + "," + lastName +"," + phone + "," + city +","+ "\n";
                //  System.out.println(registro);
                try {
                    buffwrite.write(registro);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                i++;
            }
            buffwrite.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generateFakeData(String fileName) {
        Faker faker = new Faker();
        BufferedWriter buffwrite;
        int i=0;
        String name;
        String city;
        String country;
        String phone, registro;

        try {
            FileWriter fw = new FileWriter("./results/" + fileName, true);
            buffwrite = new BufferedWriter(fw);
            while(i < 1000) {
                name = faker.name().fullName();
                phone = faker.phoneNumber().cellPhone();
                city = faker.address().cityName();
                country = faker.address().country();
                registro= name + "," +  phone + "," + city +","+ country + ",\n";
                // System.out.print(registro);
                try {
                    buffwrite.write(registro);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                i++;
            }
            buffwrite.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateFile() {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        Scanner reader = new Scanner(System.in);
        String fileName, measureUnit;
        double size, parser = 1, bytes = 0;

        decimalFormat.setMaximumFractionDigits(3);

        System.out.print("Nome do Arquivo de saida: ");
        fileName = reader.nextLine();

        File file = new File("./results/" + fileName);

        System.out.print("Unidade de medida (Bytes, KB, MB ou GB): ");
        measureUnit = reader.nextLine();

        System.out.print("Tamanho do arquivo (inteiro): ");
        size = reader.nextLong();


        long start = System.nanoTime();
        switch (measureUnit) {
            case "KB":
                parser = 1024;
                break;
            case "MB":
                parser = 1024 * 1024;
                break;
            case "GB":
                parser = 1024 * 1024 * 1024;
                break;
            default:
                System.out.println("Rodando para " + size + " Bytes.");
                break;
        }

        if (file.exists()) bytes = file.length() / parser;

        while (size > bytes) {
            this.generateFakeData(fileName);
            bytes = file.length() / parser;
        }
        long stop = System.nanoTime();
        System.out.println("Tamanho final: " + decimalFormat.format(bytes) + " " + measureUnit);
        System.out.println("Tempo total de processamento: " + ((stop - start) / (1000 * 1000 * 1000)) + "s");
    }
}

package com;

import com.code.buffer.BufferManager;
import com.code.algorithm.Merge;
import com.contact.Contact;
import com.contact.ContactBuffer;
import com.ifes.tpa.GenData;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException {
        Scanner reader = new Scanner(System.in);
        System.out.println("======== TABALHO 1 ========");
        System.out.println("1 - ADicionar dados Faker, 2 - Parte 1 do trabalho, 3 - Parte dois do trabalho");
        System.out.println("Qual ação pretende executar?");

        int action = reader.nextInt();
        reader.nextLine();
        switch (action) {
            case 1:
                GenData fake = new GenData();
                fake.populateFile();
                break;
            case 2:
                System.out.println("===== Parte 1 do trabalho (merge) =====");
                System.out.println("Nome do arquivo (ele deve estar na pasta result):");
                String filePath = reader.nextLine();
                ContactBuffer contactBuffer = new ContactBuffer();
                BufferManager<Contact> bufferManager = new BufferManager<Contact>(filePath, contactBuffer);
                bufferManager.handleSplitFile("getFullName", "getPhoneNumber", "getCity", "getCountry");
                bufferManager.genBuffers();
                Merge<Contact> merge = new Merge<>(bufferManager.getBufferArrayList().size(), bufferManager.genBufferOut(""), bufferManager);
                merge.mergeResult(null, 3);
                merge.printFileOut();
                break;
            case 3:
                System.out.println("Parte 2 do trabalho (hash)");
                System.out.println(Runtime.getRuntime().freeMemory() + " bytes");
                break;
            default:
                System.out.println("Ação não identificada.");
                break;
        }
    }
}

package com.example.tech.georgia;
import java.io.*;
import java.net.URL;

public class UniqueIPCounter {
    public static void main(String[] args) {
        try {

            String filePath = "/Users/yanderbikovmail.ru/Documents/ProjectsIDE/tech/src/main/resources/unique_ips.txt";
            URL resource = UniqueIPCounter.class.getClassLoader().getResource("files/unique_ips.txt");

            if (resource == null) {
                throw new IllegalArgumentException("Файл не найден!");
            }
            // Start timing
            long startTime = System.currentTimeMillis();

            // Initialize bitset
            long[] bitset = new long[1 << 26]; // 2^26 longs, total 512MB

            File file = new File(resource.getFile());

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Parse the IP address to int
                    int ip = parseIPAddress(line.trim());
                    // Set the corresponding bit
                    int index = ip >>> 6; // divide by 64
                    int bitPosition = ip & 63; // modulo 64
                    bitset[index] |= (1L << bitPosition);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Count the number of bits set in the bitset
            long uniqueCount = 0;
            for (long bits : bitset) {
                uniqueCount += Long.bitCount(bits);
            }

            // End timing
            long endTime = System.currentTimeMillis();

            System.out.println("Number of unique IP addresses: " + uniqueCount);
            System.out.println("Time taken: " + (endTime - startTime) + " ms");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to parse IPv4 address string to int
    private static int parseIPAddress(String ipAddress) throws NumberFormatException {
        String[] parts = ipAddress.split("\\.");
        if (parts.length != 4) {
            throw new NumberFormatException("Invalid IP address: " + ipAddress);
        }
        int ip = 0;
        for (String part : parts) {
            int value = Integer.parseInt(part);
            if (value < 0 || value > 255) {
                throw new NumberFormatException("Invalid IP address part: " + part);
            }
            ip = (ip << 8) | value;
        }
        return ip;
    }
}
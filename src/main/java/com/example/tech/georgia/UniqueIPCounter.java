package com.example.tech.georgia;
import java.io.*;
import java.net.URL;

public class UniqueIPCounter {
    public static void main(String[] args) {
        try {

            URL resource = UniqueIPCounter.class.getClassLoader().getResource("files/unique_ips.txt");

            if (resource == null) {
                throw new IllegalArgumentException("File not found");
            }
            long startTime = System.currentTimeMillis();

            long[] bitset = new long[1 << 26];

            File file = new File(resource.getFile());

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    int ip = parseIPAddress(line.trim());
                    int index = ip >>> 6;
                    int bitPosition = ip & 63;
                    bitset[index] |= (1L << bitPosition);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

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
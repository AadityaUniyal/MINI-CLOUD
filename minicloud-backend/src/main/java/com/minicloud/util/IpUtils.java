package com.minicloud.util;

public class IpUtils {
    public static boolean isValidCidr(String cidr) {
        return cidr != null && cidr.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}/[0-9]{1,2}$");
    }

    public static String generateSubnetCidr(String vpcCidr, int subnetIndex) {
        // basic CIDR subdivision logic
        String[] parts = vpcCidr.split("/");
        String[] octets = parts[0].split("\\.");
        int thirdOctet = Integer.parseInt(octets[2]) + subnetIndex;
        return octets[0] + "." + octets[1] + "." + thirdOctet + ".0/24";
    }
}

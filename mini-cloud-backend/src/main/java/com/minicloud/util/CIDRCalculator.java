package com.minicloud.util;

public class CIDRCalculator {
    
    public static boolean isValidCIDR(String cidr) {
        if (cidr == null) return false;
        String[] parts = cidr.split("/");
        if (parts.length != 2) return false;
        
        try {
            String[] octets = parts[0].split("\\.");
            if (octets.length != 4) return false;
            
            for (String octet : octets) {
                int val = Integer.parseInt(octet);
                if (val < 0 || val > 255) return false;
            }
            
            int prefix = Integer.parseInt(parts[1]);
            return prefix >= 0 && prefix <= 32;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isSubsetOf(String subnetCIDR, String vpcCIDR) {
        if (!isValidCIDR(subnetCIDR) || !isValidCIDR(vpcCIDR)) return false;
        
        long subnetStart = cidrToLong(subnetCIDR);
        long vpcStart = cidrToLong(vpcCIDR);
        long vpcEnd = vpcStart + (1L << (32 - getPrefixLength(vpcCIDR))) - 1;
        
        return subnetStart >= vpcStart && subnetStart <= vpcEnd;
    }
    
    private static long cidrToLong(String cidr) {
        String ip = cidr.split("/")[0];
        String[] parts = ip.split("\\.");
        long result = 0;
        for (String part : parts) {
            result = result * 256 + Integer.parseInt(part);
        }
        return result;
    }
    
    private static int getPrefixLength(String cidr) {
        return Integer.parseInt(cidr.split("/")[1]);
    }
}

package network;

public class ConnectionCode {

	public static long encodeIpAddress(String ipAddress) {
	    String[] parts = ipAddress.split("\\.");
	    long result = 0;
	    for (int i = 0; i < parts.length; i++) {
	        int value = Integer.parseInt(parts[i]);
	        result += ((long) value) << ((3 - i) * 8);
	    }
	    return result;
	}
	
	public static String decodeIpAddress(long encodedIpAddress) {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < 4; i++) {
	        int value = (int) ((encodedIpAddress >> ((3 - i) * 8)) & 0xFF);
	        sb.append(value);
	        if (i < 3) {
	            sb.append(".");
	        }
	    }
	    return sb.toString();
	}
	
}

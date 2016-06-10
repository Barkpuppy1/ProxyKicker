package org.s4x8.bukkit.proxykicker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.text.ParseException;

public class TorDatabase {

    private HashSet<InetAddress> torNodeList = new HashSet<InetAddress>();

    public void loadText(InputStream stream) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        int lineCount = 0;

        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            try {
                torNodeList.add(InetAddress.getByName(line));
            } catch (UnknownHostException exception) {
                throw new ParseException("Unknown IP type: " + line, lineCount);
            }
            lineCount++;
        }
    }

    public void loadBinary(InputStream stream) throws IOException, ParseException {
        int offset = 0;
        while (true) {
            int addressSize = stream.read();
            if (addressSize == -1) {
                break; // End of file
            }
            offset++;

            if (addressSize != 4 && addressSize != 16) {
                throw new ParseException("Unknown IP size: " + addressSize, offset);
            }

            byte[] ipBytes = new byte[addressSize];
            int read = stream.read(ipBytes);
            offset += read;
            if (read != addressSize) {
                throw new ParseException("Unexpected end of file", offset);
            }

            torNodeList.add(InetAddress.getByAddress(ipBytes));
        }
    }

    public void saveBinary(OutputStream stream) throws IOException {
        Iterator<InetAddress> it = torNodeList.iterator();
        while (it.hasNext()) {
            byte[] ipBytes = it.next().getAddress();
            stream.write((byte) ipBytes.length);
            stream.write(ipBytes);
        }
    }

    public boolean isTorIp(InetAddress ip) {
        return torNodeList.contains(ip);
    }
}

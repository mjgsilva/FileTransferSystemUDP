import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by mario on 07/10/14.
 */

public class Communication {
    private DatagramSocket socket;
    private DatagramPacket packetInbound;
    private DatagramPacket packetOutbound;
    private final FileCenter file;


    public Communication(int size,String destinationFolder){
        packetInbound = new DatagramPacket(new byte[size],size);
        packetOutbound = new DatagramPacket(new byte[size],size);
        file = new FileCenter(destinationFolder);
    }

    public boolean configureSocket(String ip, int port) {
        try{
            socket = new DatagramSocket();
            socket.setSoTimeout(5000);
            packetOutbound.setAddress(InetAddress.getByName(ip));
            packetOutbound.setPort(port);
            return true;
        } catch(IOException ioe) {
            System.out.println("Socket instance error: " + ioe.getMessage());
            return false;
        }
    }

    public void shutdown() {
        socket.close();
    }

    public void sendShutdownRequest() {
        try {
            packetOutbound.setData(":2".getBytes());
            packetOutbound.setLength(packetOutbound.getLength());
            socket.send(packetOutbound);
        } catch(IOException ioe) {
            System.out.println("Send shutdown request error: " + ioe);
        }
    }

    public void sendListRequest() {
        try {
            packetOutbound.setData(":1".getBytes());
            packetOutbound.setLength(packetOutbound.getLength());
            socket.send(packetOutbound);
        } catch(IOException ioe) {
            System.out.println("Send shutdown request error: " + ioe);
        }
    }

    public String receiveListBytes() {
        ByteArrayInputStream bin;
        StringBuilder listSizeBytes = new StringBuilder();
        int data;

        try {
            socket.receive(packetInbound);
            bin = new ByteArrayInputStream(packetInbound.getData());
            for (int i = 0; i < packetInbound.getLength(); i++) {
                data = bin.read();
                listSizeBytes.append((char) data);
            }
            bin.close();
        } catch (IOException ioe) {
            System.out.println("Receive list bytes error: " + ioe.getMessage());
        }
        return listSizeBytes.toString();
    }

    public String receiveList(String listSizeBytes) {
        ByteArrayInputStream bin;
        int listSize = Integer.parseInt(listSizeBytes);
        StringBuilder list = new StringBuilder();
        int it, data;

        if(listSize == 0)
            return emptyList();

        it=0;
        try {
            while (it < listSize) {
                socket.receive(packetInbound);
                bin = new ByteArrayInputStream(packetInbound.getData());
                for(int i = 0; i < packetInbound.getLength(); i++)
                {
                    data = bin.read();
                    list.append((char) data);
                }
                bin.close();
                it += packetInbound.getLength();
            }
        } catch (IOException ioe) {
            System.out.println("Receive list error: " + ioe.getMessage());
        }
        return list.toString();
    }

    public void sendDownloadRequest(String fileName) {
        try {
            packetOutbound.setData(fileName.getBytes());
            packetOutbound.setLength(packetOutbound.getLength());
            socket.send(packetOutbound);
        } catch(IOException ioe) {
            System.out.println("Send shutdown request error: " + ioe);
        }
    }

    public String downloadFileBytes() {
        int data;
        ByteArrayInputStream bin;
        StringBuilder fileSize = new StringBuilder();

        try {
            socket.receive(packetInbound);
            bin = new ByteArrayInputStream(packetInbound.getData());
            for (int i = 0; i < packetInbound.getLength(); i++) {
                data = bin.read();
                fileSize.append((char) data);
            }
            bin.close();
        } catch (IOException ioe) {
            System.out.println("Download file bytes error: " + ioe.getMessage());
        }
        return fileSize.toString();
    }

    public void downloadFile(long fileSizeBytes, String fileName) {
        FileOutputStream fos;
        BufferedOutputStream bos;
        int it;

        try {
            file.openFile(fileName, fileSizeBytes);
            packetOutbound.setData("".getBytes());

            fos = new FileOutputStream(file.getFullPath());
            bos = new BufferedOutputStream(fos);

            System.out.println(downloadStats());

            it = 0;
            while (it < fileSizeBytes) {
                socket.receive(packetInbound);
                byte[] chunk = packetInbound.getData();
                bos.write(chunk, packetInbound.getOffset(), packetInbound.getLength());
                socket.send(packetOutbound); //ack
                it += packetInbound.getLength();
            }

            bos.close();
            fos.close();

            System.out.println(downloadComplete());

        } catch (IOException ioe) {
            System.out.println("Download file error: " + ioe.getMessage());
        }

    }

    //messages

    private String emptyList() {
        return "Folder is empty!";
    }

    private String downloadStats() {
        return "----- DOWNLOAD -----\n" + "File: " + file.getFilename() + "\n" + "Expecting " + file.getFileSize() + " bytes";
    }

    private String downloadComplete() {
        return "Received: " + file.getRealFileSize() + " bytes\n" + "Download Complete!";
    }
}

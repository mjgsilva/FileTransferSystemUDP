import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by mario on 07/10/14.
 */

public class Communication {
    private DatagramSocket socket;
    private DatagramPacket packetInbound;
    private DatagramPacket packetOutbound;
    private final int port;

    public Communication(int port, int size){
        this.port = port;
        packetInbound = new DatagramPacket(new byte[size],size);
        packetOutbound = new DatagramPacket(new byte[size],size);
    }

    public boolean configureSocket() {
        try{
            socket = new DatagramSocket(port);
            return true;
        }catch(IOException e) {
            System.out.println("Socket instance error: " + e.getMessage());
            return false;
        }
    }

    public void shutdown() {
        socket.close();
    }

    // Operations
    public String getFirstRequest() {
        ByteArrayInputStream bin;
        StringBuilder firstRequest = new StringBuilder();
        int data;

        try {
            socket.receive(packetInbound);
            packetOutbound.setAddress(packetInbound.getAddress());
            packetOutbound.setPort(packetInbound.getPort());

            bin = new ByteArrayInputStream(packetInbound.getData());
            for (int i = 0; i < packetInbound.getLength(); i++) {
                data = bin.read();
                firstRequest.append((char) data);
            }
            bin.close();
        } catch (IOException e) {
            System.out.println("Socket receive error: " + e.getMessage());
        }
        return firstRequest.toString();
    }

    public boolean sendFileListBytes(String fileList) {
        boolean folderNotEmpty = false;

        try {
            if(fileList == null)
                packetOutbound.setData("0".getBytes());
            else {
                folderNotEmpty = true;
                packetOutbound.setData(fileList.getBytes());
            }
            socket.send(packetOutbound);
        }catch(IOException e){
            System.out.println("Send file list bytes error: " + e);
        }
        return folderNotEmpty;
    }

    public void sendFileList(String fileList) {
        try {
            if(fileList == null)
                packetOutbound.setData("0".getBytes());
            else
                packetOutbound.setData(fileList.getBytes());
            socket.send(packetOutbound);
        }catch(IOException e){
            System.out.println("Send file list error: " + e);
        }
    }

    public boolean sendFileBytes(Long fileSize) {
        boolean fileExists = false;

        try {
            if(fileSize != 0)
                fileExists = true;

            packetOutbound.setData(fileSize.toString().getBytes());
            socket.send(packetOutbound);
        }catch(IOException e){
            System.out.println("Send file size bytes error: " + e);
        }
        return fileExists;
    }

    public void sendFile(String path, int bufferSize) {
        int chunk;
        byte[] bArray = new byte[bufferSize];

        try {
            FileInputStream fis = new FileInputStream(path);
            BufferedInputStream bis = new BufferedInputStream(fis);

            while ((chunk = bis.read(bArray)) > -1) {
                packetOutbound.setData(bArray, 0, chunk);
                socket.send(packetOutbound);
                socket.receive(packetInbound); //ack
            }

            bis.close();
            fis.close();

        } catch (FileNotFoundException fnfe)
        { System.out.println("Send File - File Not Found: " + fnfe.getMessage()); }
        catch (IOException ioe)
        { System.out.println("Send File - IO Exception: " + ioe.getMessage()); }
    }

    //Messages
    public String messageRequest() {
        return "Client connection: " + packetInbound.getAddress() + " on port " + packetOutbound.getPort();
    }

    public String messageFileListRequest() {
        return "Client " + packetInbound.getAddress() + " requesting the list of files";
    }

    public String messageFileRequest(String file) {
        return "Client " + packetInbound.getAddress() + " requesting: " + file;
    }

    public String messageFileInfo(String fileName, Long fileSize) {
        StringBuilder msg = new StringBuilder();
        msg.append("\n----- UPLOAD -----");
        msg.append("\nFile requested: " + fileName);
        msg.append("\nFile size: " + ((fileSize / 1024) / 1024) + " Mb");
        msg.append("\nUploading...");
        return msg.toString();
    }
}

import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by mario on 06/10/14.
 */

public class Cli {
    final private String ip;
    final private int port;
    final private int bufferSize;
    final private String destinationFolder;

    public Cli(String ip, String port, String bufferSize, String destinationFolder) {
        this.ip = ip;
        this.port = Integer.parseInt(port);
        this.bufferSize = Integer.parseInt(bufferSize);
        this.destinationFolder = destinationFolder;
    }

    private String[] Tokenizer(String input){
        int i=0;
        String[] cmd = new String[3];
        StringTokenizer st = new StringTokenizer(input, " ");

        for(; (st.hasMoreElements() && i<3); i++)
            cmd[i+1] = st.nextToken();

        cmd[0] = Integer.toString(i);
        return cmd;
    }

    public void Execute()
    {
        Communication com = new Communication(bufferSize,destinationFolder);

        BufferedReader reader;
        String input;
        String[] cmd;

        if(!com.configureSocket(ip,port))
            return;

        System.out.println("Commands list:\nlist (List of all files)\ndownload <filename>\nshutdown");

        while(true)
        {
            System.out.print("~> ");
            reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                input = reader.readLine();
                cmd = Tokenizer(input);
                if(Integer.parseInt(cmd[0]) > 0)
                    switch (cmd[1]) {
                        case "shutdown":
                            com.sendShutdownRequest();
                            com.shutdown();
                            return;
                        case "list":
                            com.sendListRequest();
                            System.out.println(com.receiveList(com.receiveListBytes()));
                            break;
                        case "download":
                            com.sendDownloadRequest(cmd[2]);
                            long size = Long.parseLong(com.downloadFileBytes());
                            if(size > 0)
                                com.downloadFile(size, cmd[2]);
                            else
                                System.out.println("File not found");
                            break;
                        default:
                            System.out.println("Invalid command");
                            continue;
                    }
                else
                    System.out.println("Invalid number of arguments!");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}

/**
 * Created by mario on 06/10/14.
 */
public class Init {
    public static void main(String[] args)
    {
        String ip = args[0];
        String port = args[1];
        String bufferSize = args[2];
        String destinationFolder = args[3];

        //Cli cli = new Cli("127.0.0.1","5000","512");

        Cli cli = new Cli(ip,port,bufferSize,destinationFolder);
        cli.Execute();
    }
}

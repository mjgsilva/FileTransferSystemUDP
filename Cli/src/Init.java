/**
 * Created by mario on 06/10/14.
 */
public class Init {
    public static void main(String[] args)
    {
        //String ip = args[1];
        //int port = Integer.parseInt(args[2]);
        //int bufferSize = Integer.parseInt(args[3]);

        Cli cli = new Cli("127.0.0.1","5000","512");

        //Cli cli = new Cli(ip,port,bufferSize);
        cli.Execute();
    }
}

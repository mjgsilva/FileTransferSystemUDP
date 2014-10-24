/**
 * Created by mario on 06/10/14.
 */
public class Init {
    public static void main(String[] args)
    {
        String ip = args[0];
        String port = args[1];
        String destinationFolder = args[2];

        Cli cli = new Cli(ip,port,512,destinationFolder);
        cli.Execute();
    }
}

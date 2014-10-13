/**
 * Created by mario on 06/10/14.
 */
public class Init {
    public static void main(String[] args)
    {
        System.out.println("Server's ON");
        //String port = args[1];
        //String dir = args[2];
        //String bufferSize = args[3];

        Srv srv = new Srv("5000","/Users/mario/Downloads/","512");

        //Srv srv = new Srv(port,dir,bufferSize);
        srv.Execute();
    }
}

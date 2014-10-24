/**
 * Created by mario on 06/10/14.
 */
public class Init {
    public static void main(String[] args)
    {
        System.out.println("Server's ON");
        String port = args[0];
        String dir = args[1];
        String bufferSize = 512;

        Srv srv = new Srv(port,dir);
        srv.Execute();
    }
}

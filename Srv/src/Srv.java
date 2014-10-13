/**
 * Created by mario on 06/10/14.
 */

/* File root = new File(rootDirectory).getCanonicalFile();
String name = root + File.separator+req;
File file = new File(name).getCanonicalFile();
if(!file.getAbsolutePath().startsWith(root.getAbsolutePath())){..}
 */

public class Srv {
    private final int port;
    private final String dir;
    private final int bufferSize;

    public Srv(String port, String dir, String size){
        this.port = Integer.parseInt(port);
        this.dir = dir;
        this.bufferSize = Integer.parseInt(size);
    }

    public void Execute() {

        Communication com = new Communication(port, bufferSize);
        FileCenter fCenter = new FileCenter(dir);
        String request;

        if (!com.configureSocket())
            return;

        while (true) {
            /* First Request: :2 - Shutdown; :1 - List all the files; x - Filename */

            request = com.getFirstRequest(); // Wait for the first request
            System.out.println(com.messageRequest());

            if (request.equals(":2")) {
                com.shutdown();
                return;
            }
            else if (request.equals(":1")) {
                System.out.println(com.messageFileListRequest());
                String fileList = fCenter.getFileList(); // null - empty folder
                if (com.sendFileListBytes(fileList)) // fileList != null send it
                    com.sendFileList(fileList);
            } else {
                Long fileSize;
                System.out.println(com.messageFileRequest(request));
                fileSize = fCenter.openFile(request);
                if (com.sendFileBytes(fileSize)) // 0 - file not found
                {
                    System.out.println(com.messageFileInfo(fCenter.getFileName(), fCenter.getFileSizeLong()));
                    com.sendFile(fCenter.getFullPath(), bufferSize);
                }
            }
        }
    }
}

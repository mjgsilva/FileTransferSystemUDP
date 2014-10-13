import java.io.File;

/**
 * Created by mario on 07/10/14.
 */
public class FileCenter {
    private String dir;
    private String fileName;
    private String fullPath;
    private File downloadFile;
    private long fileSize;

    public FileCenter(String dir){
        this.dir = dir;
    }

    public Long openFile(String filename)
    {
        this.fileName = filename;
        this.fullPath = dir+filename;
        downloadFile = new File(fullPath);

        if(downloadFile.exists() && !downloadFile.isDirectory())
            fileSize = downloadFile.length();
        else
            fileSize = 0;
        return fileSize;
    }

    public Long getFileSizeLong()
    {
        return fileSize;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getFullPath()
    {
        return dir+fileName;
    }

    public String getFileList() {
        StringBuilder list = new StringBuilder();
        File folder = new File("/Users/mario/Downloads/");
        File[] fileList = folder.listFiles();

        for(int i=0; i < fileList.length; i++)
        {
            if(fileList[i].isFile() && (!fileList[i].isHidden())) {
                list.append(fileList[i].getName());
                list.append("\n");
            }
        }

        if(list.length() == 0)
            return null;
        else
            return list.toString();
    }
}

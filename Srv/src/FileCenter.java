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

    public String getFileSize()
    {
        return Long.toString(fileSize);
    }

    public Long getFileSizeLong()
    {
        return fileSize;
    }

    public String getFileName()
    {
        return fileName;
    }

    public File getDownloadFile() { return downloadFile; }

    public String getFullPath()
    {
        return dir+fileName;
    }

    public String getFileList() {
        StringBuilder list = null;
        File folder = new File(dir);
        File[] fileList = folder.listFiles();

        for(int i=0; i < fileList.length; i++)
        {
            if(fileList[i].isFile())
                list.append(fileList[i].getName());
        }

        if(list == null)
            return null;
        else
            return list.toString();
    }
}

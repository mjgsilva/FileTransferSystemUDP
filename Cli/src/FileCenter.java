import java.io.File;

/**
 * Created by mario on 07/10/14.
 */

public class FileCenter {
    private final String dir;
    private String fileName;
    private String fullPath;
    private Long fileSize;
    private File downloadFile;

    public FileCenter(String destinationFolder){
        this.dir = destinationFolder;
    }

    public void openFile(String fileName, Long fileSize)
    {
        this.fileName = fileName;
        this.fullPath = dir+fileName;
        downloadFile = new File(fullPath);
        this.fileSize = fileSize;
    }

    public String getFilename() { return fileName; }

    public String getFullPath()
    {
        return dir+fileName;
    }

    public Long getFileSize() { return fileSize; }

    public Long getRealFileSize() { return downloadFile.length(); }

}
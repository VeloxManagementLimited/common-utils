package exchange.velox.commonutils;

import java.io.Serializable;

public class FileDTO implements Serializable {
    private String fileName;
    private byte[] data;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}

package exchange.velox.commonutils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.xml.ws.BindingType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;

public class VeloFileUtilsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void testUnzipFileSuccess() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("file1.zip");
        byte[] bytes = IOUtils.toByteArray(is);
        List<FileDTO> unzippedFileDTOS = VeloFileUtils.unzipFile("file1.zip", bytes, null);
        Assert.assertEquals("asd.txt", unzippedFileDTOS.get(0).getFileName());
    }

    @Test
    public void testUnzipFileWithPassword() throws IOException {
        InputStream is = this.getClass()
                             .getClassLoader()
                             .getResourceAsStream("password-protected.zip");
        byte[] bytes = IOUtils.toByteArray(is);
        List<FileDTO> unzippedFileDTOS = VeloFileUtils.unzipFile("password-protected.zip", bytes,
                                                                 "somesecret");
        Assert.assertEquals("asd.txt", unzippedFileDTOS.get(0).getFileName());
        
        String expectedContentStr = "asd";
        Assert.assertArrayEquals(expectedContentStr.getBytes(), unzippedFileDTOS.get(0).getData());
    }

    @Test
    public void testUnzipFile_NotAZip() throws IOException {
        InputStream is = this.getClass()
                             .getClassLoader()
                             .getResourceAsStream("not-a-zip.txt");
        byte[] bytes = IOUtils.toByteArray(is);
        List<FileDTO> unzippedFileDTOS = VeloFileUtils.unzipFile("not-a-zip.txt", bytes, null);
        Assert.assertEquals("not-a-zip.txt", unzippedFileDTOS.get(0).getFileName());

        String expectedContentStr = "asd";
        Assert.assertArrayEquals(expectedContentStr.getBytes(), unzippedFileDTOS.get(0).getData());
    }

    @Test
    public void testUnzipFileFailed_WrongPassword() throws IOException {
        expectedException.expectCause(isA(net.lingala.zip4j.exception.ZipException.class));
        
        InputStream is = this.getClass()
                             .getClassLoader()
                             .getResourceAsStream("password-protected.zip");

        byte[] bytes = IOUtils.toByteArray(is);
        List<FileDTO> unzippedFileDTOS = VeloFileUtils.unzipFile("password-protected.zip", bytes,
                                                                 "wrongpassword");
    }

    @Test
    public void testUnzipFileFailed_MissingPassword() throws IOException {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("This file is password protected, please input password.");

        InputStream is = this.getClass()
                             .getClassLoader()
                             .getResourceAsStream("password-protected.zip");

        byte[] bytes = IOUtils.toByteArray(is);
        List<FileDTO> unzippedFileDTOS = VeloFileUtils.unzipFile("password-protected.zip", bytes, "");
    }

    @Test
    public void testUnzipFile_7Zip() throws IOException {
        InputStream is = this.getClass()
                             .getClassLoader()
                             .getResourceAsStream("commpress-with-sevenz.7z");
        byte[] bytes = IOUtils.toByteArray(is);
        List<FileDTO> unzippedFileDTOS = VeloFileUtils.unzipFile("commpress-with-sevenz.7z", bytes, null);
        Assert.assertEquals("commpress-with-sevenz.7z", unzippedFileDTOS.get(0).getFileName());
    }

    @Test
    public void testZipFileWithPassword() throws IOException {
        String filename = "not-a-zip.txt";
        InputStream is = this.getClass()
                             .getClassLoader()
                             .getResourceAsStream(filename);
        byte[] bytes = IOUtils.toByteArray(is);
        FileDTO fileDTO = new FileDTO();
        fileDTO.setData(bytes);
        fileDTO.setFileName(filename);
                
        byte[] zipBytes = VeloFileUtils.zipFiles(Arrays.asList(fileDTO), "123456");
        List<FileDTO> unzippedFileDTOS = VeloFileUtils.unzipFile("commpress.zip", zipBytes, "123456");
        Assert.assertEquals(filename, unzippedFileDTOS.get(0).getFileName());
    }
}

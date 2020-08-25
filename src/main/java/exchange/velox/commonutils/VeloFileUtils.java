package exchange.velox.commonutils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class VeloFileUtils {

    /**
     * Unzip file
     *
     * @param zipBytes compressed files
     * @param password is used if zip file is encrypted
     * @return metadata of files
     * @throws IOException
     */
    public static List<FileDTO> unzipFile(String zipFileName, byte[] zipBytes, String password) {
        List<FileDTO> result = new ArrayList<>();
        if (ArrayUtils.isEmpty(zipBytes)) {
            return new ArrayList<>();
        }
        
        // create temp folder 
        String tempDirectoryPath = FileUtils.getTempDirectoryPath() + File.separator + UUID.randomUUID().toString();
        File executionDir = new File(tempDirectoryPath);
        if (!executionDir.mkdir() || !executionDir.exists()) {
            throw new RuntimeException("Could not create temp dir " + tempDirectoryPath);
        }

        try {
            // save bytes as zip file
            String filePath = tempDirectoryPath + File.separator + zipFileName + ".zip";
            File compressedFile = new File(filePath);
            FileUtils.writeByteArrayToFile(compressedFile, zipBytes);

            // unzip for encrypted or not
            ZipFile zipFile = new ZipFile(compressedFile);
            
            if (!zipFile.isValidZipFile()) {
                FileDTO unzippedFileDTO = new FileDTO();
                unzippedFileDTO.setData(zipBytes);
                unzippedFileDTO.setFileName(zipFileName);
                result.add(unzippedFileDTO);
                return result;
            }
            
            if (zipFile.isEncrypted()) {
                if (StringUtils.isBlank(password)) {
                    throw new RuntimeException("This file is password protected, please input password.");
                }
                zipFile.setPassword(password);
            }
            
            List fileHeaderList = zipFile.getFileHeaders();
            for (int i = 0; i < fileHeaderList.size(); i++) {
                FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
                // Path where you want to Extract
                zipFile.extractFile(fileHeader, tempDirectoryPath);
            }

            // when unzip is successful, read files as list of byte + file name + file extension
            Collection<File> fileList = FileUtils.listFiles(executionDir, null, true);
            for (File file : fileList) {
                if (!file.isDirectory() &&
                    !FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("zip")) {

                    FileDTO unzippedFileDTO = new FileDTO();
                    unzippedFileDTO.setFileName(file.getName());
                    unzippedFileDTO.setData(FileUtils.readFileToByteArray(file));
                    result.add(unzippedFileDTO);
                }
            }
        } catch (ZipException | IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            // delete temp folder anyway
            FileUtils.deleteQuietly(executionDir);
        }

        return result;
    }

    public static byte[] zipFiles(List<FileDTO> files) {
        return zipFiles(files, null, null, false);
    }

    public static byte[] zipFiles(List<FileDTO> files, String password) {
        return zipFiles(files, password, null, false);
    }

    public static byte[] zipFiles(List<FileDTO> files, String password, String folderName, boolean compressFolder) {
        String tempDirectoryPath = FileUtils.getTempDirectoryPath() + File.separator + UUID.randomUUID().toString();
        File executionDir = new File(tempDirectoryPath);
        if (!executionDir.mkdir() || !executionDir.exists()) {
            throw new RuntimeException("Could not create temp dir " + tempDirectoryPath);
        }

        try {
            String filesDirPath;
            if (StringUtils.isBlank(folderName)) {
                filesDirPath = tempDirectoryPath + File.separator + "zip_temp";
            } else {
                filesDirPath = tempDirectoryPath + File.separator + folderName;
            }
            File filesDir = new File(filesDirPath);
            filesDir.mkdir();

            for (FileDTO fileDTO : files) {
                String filePath = filesDirPath + File.separator + fileDTO.getFileName();
                FileUtils.writeByteArrayToFile(new File(filePath), fileDTO.getData());
            }

            // zip executionDir
            String destinationZipFilePath = filesDirPath + ".zip";
            File destination = new File(destinationZipFilePath);
            addFilesToZip(filesDir, destination, password, compressFolder);

            return FileUtils.readFileToByteArray(new File(destinationZipFilePath));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            FileUtils.deleteQuietly(executionDir);
        }
    }

    /**
     * Add all files from the source directory to the destination zip file
     *
     * @param source      the directory with files to add
     * @param destination the zip file that should contain the files
     * @throws ZipException      if destination is null
     */
    private static void addFilesToZip(File source, File destination, String password,
                                      boolean compressFolder) throws ZipException {
        Collection<File> fileList = FileUtils.listFiles(source, null, true);
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        
        if (StringUtils.isNotBlank(password)) {
            zipParameters.setEncryptFiles(true);
            zipParameters.setPassword(password.toCharArray());
            zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        }
        
        ZipFile zipFile = new ZipFile(destination);
        if (compressFolder) {
            zipFile.createZipFileFromFolder(source, zipParameters, false, 0L);
        } else {
            zipFile.addFiles(new ArrayList<>(fileList), zipParameters);
        }
    }

    /**
     * Remove the leading part of each entry that contains the source directory name
     *
     * @param source the directory where the file entry is found
     * @param file   the file that is about to be added
     * @return the name of an archive entry
     * @throws IOException if the io fails
     */
    private static String getEntryName(File source, File file) throws IOException {
        int index = source.getAbsolutePath().length() + 1;
        String path = file.getCanonicalPath();
        return path.substring(index);
    }

    /**
     * Decode url, using when pass parameters on rest-template
     *
     * @param urlString
     * @return
     */
    public static String decodeURLString(String urlString) {
        if (urlString != null) {
            try {
                return URLDecoder.decode(urlString, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return urlString;
    }

    /**
     * Encode url, using when pass parameters on rest-template
     *
     * @param urlString
     * @return
     */
    public static String encodeURLString(String urlString) {
        if (urlString != null) {
            try {
                return URLEncoder.encode(urlString, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return urlString;
    }
}


























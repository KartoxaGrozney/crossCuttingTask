import javax.crypto.*;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class AuxiliaryАunctions {
    public static void archiveFileZip(String fileName, String archiveName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(archiveName);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(fileName)) {
            ZipEntry zipEntry = new ZipEntry(new File(fileName).getName());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }

    public static void unzipFile(String zipFileName) {
        // Получаем путь к папке проекта
        String projectDir = System.getProperty("user.dir");
        File zipFile = new File(projectDir, zipFileName);
        File destDirectory = new File(projectDir, "extractedZip"); // Папка для разархивации

        if (!zipFile.exists()) {
            System.out.println("Файл не найден: " + zipFile.getAbsolutePath());
            return;
        }

        if (!destDirectory.exists()) {
            destDirectory.mkdirs(); // Создаём директорию, если она не существует
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                File newFile = new File(destDirectory, entry.getName());

                // Если это директория, создаём её
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    // Создаём все необходимые родительские директории
                    new File(newFile.getParent()).mkdirs();

                    // Записываем файл
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zipInputStream.closeEntry();
            }
            System.out.println("Разархивация завершена в папку: " + destDirectory.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createRar(String rarFileName, String fileToArchive) {
        try {
            Runtime.getRuntime().exec(new String[] {"C:\\Soft\\WinRar\\Rar.exe", "a", rarFileName, fileToArchive});
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void unrarFile(String rarFileName, String outputDirectory) {
        try {
            File outputFolder = new File(outputDirectory);
            if (!outputFolder.exists()) {
                outputFolder.mkdirs();
            }
            String currentDirectory = outputFolder.getCanonicalPath();
            Runtime.getRuntime().exec(new String[] {"C:\\Soft\\WinRar\\UnRAR.exe", "e", rarFileName, currentDirectory});
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // длина ключа
        return keyGen.generateKey();
    }

    public static void encryptFile(String filePath, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        try (FileInputStream fis = new FileInputStream(filePath);
             FileOutputStream fos = new FileOutputStream(filePath + ".enc");
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        }
    }

    public static void decryptFile(String filePath, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        try (FileInputStream fis = new FileInputStream(filePath);
             CipherInputStream cis = new CipherInputStream(fis, cipher);
             FileOutputStream fos = new FileOutputStream(filePath.replace(".enc", ".dec"))) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
}

import java.io.File;
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
        File destDirectory = new File(projectDir, "extracted"); // Папка для разархивации

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
}

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class informationProcessing {
    public static void main(String[] args) {
        String fileOut = "output.txt", fileIn = "input.txt";

        MathematicalExpression operation = new MathematicalExpression();

        operation.readerFromFile(fileIn);
        operation.evaluate();
        //operation.evaluateWithRegular();
        //operation.believesWithLib();
        operation.writerToFile(fileOut);

        try {
            AuxiliaryАunctions.archiveFileZip(fileOut, "arch.zip");
            AuxiliaryАunctions.unzipFile("arch.zip");
            AuxiliaryАunctions.createRar("archiv.rar", fileOut);
            AuxiliaryАunctions.unrarFile("archiv.rar", "extractedRar");
            SecretKey myKey = AuxiliaryАunctions.generateKey();
            AuxiliaryАunctions.encryptFile(fileOut, myKey);
            AuxiliaryАunctions.encryptFile("archiv.rar", myKey);
            AuxiliaryАunctions.decryptFile("output.txt.enc", myKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

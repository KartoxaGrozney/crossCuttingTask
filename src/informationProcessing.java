import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class informationProcessing {
    private static String fileOut = "output.";
    private static String fileIn = "input.";
    private static double expValue;

    public static void setFileName(String inpFile, String outFile){
        fileIn += inpFile;
        fileOut += outFile;
    }

    public static void setExpValue(double expValueNew){
        expValue = expValueNew;
    }

    public static double getExpValue(){
        return expValue;
    }

    public static String getFileIn(){
        return fileIn;
    }

    public static String getFileOut(){
        return fileOut;
    }

    public static void mainFoo() {
        MathematicalExpression operation = new MathematicalExpression();

        if(fileIn.equals("input.txt")) {
            operation.readerFromFile(fileIn);
        }
        else if(fileIn.equals("input.xml")) {
            operation.readXmlFile(fileIn);
        }
        else if(fileIn.equals("input.html")) {
            operation.readHtmlFile(fileIn);
        }
        else if(fileIn.equals("input.json")) {
            operation.readerFromJSON(fileIn);
        }
        else if(fileIn.equals("input.yaml")) {
            operation.readerFromYAML(fileIn);
        }
        operation.evaluate();

        //operation.evaluateWithRegular();
        //operation.believesWithLib();
        setExpValue(operation.getResExp());

        if(fileIn.equals("output.txt")) {
            operation.writerToFile(fileOut);
        }
        else if(fileIn.equals("output.xml")) {
            operation.writerToFileXML(fileOut);
        }
        else if(fileIn.equals("output.html")) {
            operation.writeToHtmlFile(fileOut);
        }
        else if(fileIn.equals("output.json")) {
            operation.writeToJsonFile(fileOut);
        }
        else if(fileIn.equals("output.yaml")) {
            operation.writeToYamlFile(fileOut);
        }

        if(fileOut.equals("output.txt")) {
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
}

package main.java;

// Java.io
import java.io.File;
import java.io.IOException;

// Java.util
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

// JavaX.crypto  and Java.security
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

// PDDocument PDFTextReader
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class CryptoText {
    // Attributes
    private String fileNameToSearch;
    private List<String> result = new ArrayList<String>();

    public static void main (String args[]) throws Exception {
        // Scanner and User Input
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the name of your file (i.e. filename.pdf): ");
        String fileName = input.nextLine();

        // Exits program if file isn't a PDF
        if (!fileName.endsWith(".pdf")) {
            System.out.println("File is not a pdf!");
            System.exit(0);
        }

        // Prints filename
        System.out.println(fileName);

        // Searches for file
        CryptoText fileSearch = new CryptoText();
        fileSearch.searchDirectory(new File("/Users/User/Desktop"), fileName);

        String filePath = "";
        int count = fileSearch.getResult().size();
        if (count == 0) {
            System.out.println("\nNo result found!");
        }

        else {
            System.out.println("\nFound " + count + " result!\n");
            for (String matched : fileSearch.getResult()) {
                System.out.println("Found! File path is " + matched);
                filePath = filePath + matched;
            }
        }

        // Encrypts File Text and Displays It
        String textDecrypt = CryptoText.extractText(filePath);
        String textEncrypt = CryptoText.encrypt(textDecrypt);
        System.out.println(textEncrypt);
    }

    public static String extractText(String filePath) throws IOException {
        // Loads PDF File
        File file = new File(filePath);
        PDDocument document = PDDocument.load(file);

        // Extracts Text from PDF and returns it
        PDFTextStripper pdfStripper = new PDFTextStripper();

        String text = pdfStripper.getText(document);
        return text;
    }

    public static String encrypt(String text) throws Exception {
        // Generate Key
        String key = "1234567890pizzas"; // 128 bit key

        // Create Cipher
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        // Encrypt and return text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        return(new String(encrypted));
    }

    public void searchDirectory(File directory, String fileNameToSearch) {
        setFileNameToSearch(fileNameToSearch);

        // Determines if directory exists
        if (directory.isDirectory()) {
            search(directory);
        }

        else {
            System.out.println(directory.getAbsoluteFile() + " is not a directory!");
        }
    }

    private void search(File file) {
        if (file.isDirectory()) {
            System.out.println("Searching directory ... " + file.getAbsoluteFile());

            // Determines access to a direction
            if (file.canRead()) {
                for (File temp : file.listFiles()) {
                    if (temp.isDirectory()) {
                        search(temp);
                    }

                    else {
                        if (getFileNameToSearch().equals(temp.getName())) {
                            result.add(temp.getAbsoluteFile().toString());
                        }
                    }
                }
            }

            else {
                System.out.println(file.getAbsoluteFile() + "Permission Denied");
            }
        }
    }

    public String getFileNameToSearch() {
        return fileNameToSearch;
    }

    public void setFileNameToSearch(String fileNameToSearch) {
        this.fileNameToSearch = fileNameToSearch;
    }

    public List<String> getResult() {
        return result;
    }
}
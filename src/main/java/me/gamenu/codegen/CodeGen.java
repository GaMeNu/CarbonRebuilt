package me.gamenu.codegen;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CodeGen {

    private static final String GENDATA_FILENAME = "gendata.json";


    private final File actiondump;
    private final File outDir;

    public CodeGen(String actiondumpPath, String outputDirectryPath) {
        this.actiondump = new File(actiondumpPath);
        this.outDir = new File(outputDirectryPath);
    }



    public boolean generate() {
        try {
            //Rebuild output package
            rebuildOutputPkg();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (checkADFileChanged()) regenerateContent();

        try {
            writeGenData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private void regenerateContent() {

    }

    private void rebuildOutputPkg() throws IOException {
        if (!outDir.exists()) {
            outDir.mkdir();
        }

        File dataFile = new File(outDir, GENDATA_FILENAME);
        writeGenData();

    }


    private void writeGenData() throws IOException {
        String toWrite = checkADChecksum();
        File dataFile = new File(outDir, GENDATA_FILENAME);

        if (!dataFile.exists()) {
            dataFile.createNewFile();
            toWrite = "";
        }

        JSONObject gendata = new JSONObject()
                .put("ad_checksum", toWrite);

        String gendataStr = gendata.toString(2);

        System.out.println(gendata);
        try (FileWriter fw = new FileWriter(dataFile)) {
            // write an empty string to force regeneration of the DBC after package is built.
            fw.append(gendataStr);
            fw.flush();
        }
    }

    /**
     * Check whether the ActionDump file's hash was changed
     * @return whether the file was changed since last run
     * @throws FileNotFoundException If the ActionDump file was not found
     */
    private boolean checkADFileChanged() {
        // Open the generated data file, and get the stored checksum
        JSONObject data = null;
        try {
            data = getGenData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String oldChecksum = data.getString("ad_checksum");
        String currentChecksum = checkADChecksum();
        return (!oldChecksum.equals(currentChecksum));
    }

    /**
     * Returns a JSON object containing all generation data
     * @return the gendata JSON object
     * @throws FileNotFoundException If the gendata file does not exist
     */
    private JSONObject getGenData() throws FileNotFoundException {
        File gendataFile = new File(outDir, GENDATA_FILENAME);
        JSONTokener tk = new JSONTokener(new FileInputStream(gendataFile));
        JSONObject data = new JSONObject(tk);
        return data;
    }

    /**
     * Get the MD5 Checksum of the ActionDump file
     * @return the MD5 Checksum of the ActionDump file
     */
    private String checkADChecksum() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try (InputStream is = new FileInputStream(actiondump);
             DigestInputStream dis = new DigestInputStream(is, md)
        ) {
            byte[] buf = new byte[1024];

            //noinspection StatementWithEmptyBody
            while (dis.read(buf) != -1);

            byte[] hash = md.digest();

            return bytesToHex(hash);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    /**
     * Converts a byte array to a hexadecimal string representing it.
     * Each pair of characters in the result string represents a single byte.
     * @param bytes byte array to convert
     * @return A hexadecimal representation of the byte array
     */
    // Credit to stackoverflow
    private static String bytesToHex(byte[] bytes) {
        // create a new char array, twice the size of the byte array
        char[] hexChars = new char[bytes.length * 2];

        // Iterate over every byte
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF; // Get only the 1s, remove negative values
            hexChars[j * 2] = HEX_ARRAY[v >>> 4]; // Get the left nibble, and place the corresponding hex char in the first of the char pair
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F]; // Get the right nibble, and place the corresponding hex char in the second of the char pair
        }

        return new String(hexChars);
    }
}

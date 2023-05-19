package AES;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionManager {

    private String path;

    public EncryptionManager(String path, SecretKey key, IvParameterSpec iv) {
        this.path = path;
        this.key = key;
        Iv = iv;
    }

    public EncryptionManager() {
    }

    private SecretKey key;
    IvParameterSpec Iv;



    /**
     * used to display the secret key
     * @param secretKey
     * @return
     */
    private String checkKey( SecretKey secretKey){
        byte[] data = secretKey.getEncoded();
        String key = Base64.getEncoder().encodeToString(data);
        return key;
    }

    public void encryptFile(String path, SecretKey key, IvParameterSpec Iv)  {

        File inputFile = new File(path);

        if (inputFile.length() == 0 || !inputFile.exists()) {
            System.out.println("File not found or empty");

        }
        if (key == null) {
            System.out.println("Enter a valid key");
        }
        FileOutputStream outputStream = null;
        FileInputStream inputStream = null;

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, Iv); // initialize the cipher into

            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(path + ".encrypted");

            CipherOutputStream cis = new CipherOutputStream(outputStream, cipher);
            byte[] buffer = new byte[1024]; // how much data to read
            int count;
            while ((count = inputStream.read(buffer)) != -1) {
                cis.write(buffer, 0, count);
            }
            cis.flush();
            System.out.println("File is encrypted !!!\nHere is the key: "+ checkKey(key));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.out.println("Encryption failed do to padding error");

        } catch (IOException e){
            System.out.println("error found with the file");
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("File encryption failed due to an Invalid algorithm parm: " + e.getMessage());

        } catch (InvalidKeyException e) {
            System.out.println("File encryption failed due to an Invalid key: " + e.getMessage());
        } finally {
            /*if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }*/
        }

    }

    public void decryptFile(String path, SecretKey key, IvParameterSpec Iv)  {

        File inputFile = new File(path);

        if (inputFile.length() == 0 || !inputFile.exists()) {
            System.out.println("File not found or empty");
        }
        if(!path.contains(".encrypted")){
            System.out.println("Please select a file previously encrypted");
            return;
        }
        if (key == null) {
            System.out.println("Enter a valid key");
        }
        FileOutputStream outputStream = null;
        FileInputStream inputStream = null;
        CipherInputStream cis = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, Iv); // initialize the cipher

            outputStream = new FileOutputStream(path + ".unencrypted");
            inputStream = new FileInputStream(inputFile);
            cis = new CipherInputStream(inputStream, cipher);


            byte[] buffer = new byte[1024]; // how much data to read
            int count;
            while ((count = cis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }
            outputStream.flush();
            System.out.println("File is decrypted !!!");

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.out.println("Encryption failed do to padding error");

        } catch (IOException e){
            System.out.println("error found with the file");
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("File encryption failed due to an Invalid algorithm parm: " + e.getMessage());

        } catch (InvalidKeyException e) {
            System.out.println("File encryption failed due to an Invalid key: " + e.getMessage());
        } finally {
           /* if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }*/

        }

    }


}

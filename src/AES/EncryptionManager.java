package AES;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class EncryptionManager {

    static final int IV_LENGTH = 16;
    static final int SALT_LENGTH = 20;

    private int keyLength;

    public EncryptionManager() {
    }

    AES_Utils aesUtils = new AES_Utils();
    IvParameterSpec Iv;

    /**
     * Print the Iv
     *
     * @param iv
     * @return
     */
    public String iv(IvParameterSpec iv) {
        byte[] data = iv.getIV();
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * used to display the secret key
     *
     * @param secretKey
     * @return
     */
    private String checkKey(SecretKey secretKey) {
        byte[] data = secretKey.getEncoded();
        String key = Base64.getEncoder().encodeToString(data);
        return key;
    }

    public void encryptFile(String path, String password) {
        this.keyLength = keyLength;
        File inputFile = new File(path);

        if (inputFile.length() == 0 || !inputFile.exists()) {
            System.out.println("File not found or empty");

        }

        //generate the salt values
        byte[] salt = aesUtils.createSalt();

        // get the Iv
        IvParameterSpec iv = aesUtils.initializationVector();

        //create a key from a given password
        SecretKey key = aesUtils.getKeyFromPassword(password.toCharArray(), salt);

        if (key == null) {
            System.out.println("Error creating key");
        }
        FileOutputStream outputStream = null;
        FileInputStream inputStream = null;

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, Iv); // initialize the cipher

            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(path + ".aes");

            //prefix the iv and salt  to the file
            outputStream.write(iv.getIV());
            outputStream.write(salt);

            CipherOutputStream cis = new CipherOutputStream(outputStream, cipher);
            byte[] buffer = new byte[1024]; // how much data to read
            int count;

           /* byte[] ivBytes;
            ivBytes = iv.getIV();
            byte[] encryptedIv = cipher.doFinal(ivBytes);
            outputStream.write(encryptedIv);*/

            while ((count = inputStream.read(buffer)) != -1) {
                cis.write(buffer, 0, count);
            }
            cis.flush();
            System.out.printf("File is encrypted\n");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.out.println("Encryption failed do to padding error");

        } catch (IOException e) {
            System.out.println("error found with the file");
            e.getStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("File encryption failed due to an Invalid algorithm: " + e.getMessage());

        } catch (InvalidKeyException e) {
            System.out.println("File encryption failed due to an Invalid key: " + e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("Error occurred while closing the streams");
            }
        }

    }

    public void decryptFile(Scanner scan) {
        System.out.print("Enter the file path: ");
        String path = scan.next();

        if (!path.contains(".aes")) {
            System.out.println("Please select a file previously encrypted");
            return;
        }
        System.out.print("Enter a password: ");
        String password  = scan.next();
       // System.out.println(password);
        File inputFile = new File(path);

        if (!inputFile.exists() || inputFile.length() == 0) {
            System.out.println("File not found or empty");
        }


       /* if (key == null) {
            System.out.println("Enter a valid key");
        }*/


        FileOutputStream outputStream = null;
        FileInputStream inputStream = null;
        CipherInputStream cis = null;


        try {
            outputStream = new FileOutputStream(path + ".unencrypted");
            inputStream = new FileInputStream(inputFile);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cis = new CipherInputStream(inputStream, cipher);

            //get the Iv and salt from the file
            byte[] iv = new byte[IV_LENGTH];
            byte[] salt = new byte[SALT_LENGTH];


            int bytesRead = inputStream.read(iv);
            if (bytesRead != IV_LENGTH) {
                System.out.println("Error reading the encrypted IV from the file");
                return;
            }
            StringBuilder si = new StringBuilder();

            // System.out.println("Heres the decrypte iv"+Base64.getEncoder().encodeToString(iv));

            bytesRead = inputStream.read(salt);
            if (bytesRead != SALT_LENGTH) {
                System.out.println("Error reading the encrypted salt from the file");
                return;
            }
            StringBuilder ss = new StringBuilder();

            for (byte i : salt) {
                ss.append(i);
            }
            System.out.println("Here'ss the decrypted salt: " + ss);

            //System.out.println("Heres the decrypte salt"+salt);
            /*for( byte i : salt){
                System.out.println("Heres the decrypte salt"+i);
            }*/

            SecretKey key = aesUtils.getKeyFromPassword(password.toCharArray(), salt);


            //cipher.init(Cipher.DECRYPT_MODE, key); // initialize the cipher

            IvParameterSpec ivDecrypt = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivDecrypt);


            byte[] buffer = new byte[1024]; // how much data to read
            int count;
            while ((count = cis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }

            
            outputStream.flush();
            System.out.println("File is decrypted !!!");
            outputStream.close();
            inputStream.close();

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.out.println("Encryption failed do to padding error");

        } catch (IOException e) {
            System.out.println("Unable to decrypt the file");

        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("File encryption failed due to an Invalid algorithm parm: " + e.getMessage());
            e.getStackTrace();
        } catch (InvalidKeyException e) {
            System.out.println("File encryption failed due to an Invalid key: ");
            e.printStackTrace();
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

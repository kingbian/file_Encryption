package AES;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Scanner;

public class AES_Encryption {
    private String password;
    private int keyLength;
    private String path;

    Scanner scan = new Scanner(System.in);

    EncryptionManager em;

    public void encrypt(){
        System.out.print("Enter the file path: ");
        path = scan.next();
        System.out.print("Enter a password: ");
        password = scan.next();
        System.out.print("Enter a key length: ");
        keyLength = scan.nextInt();
        em = new EncryptionManager();
        // call the encryption method
        em.encryptFile(path, getKeyFromPassword(), initializationVector());

    }
    // TODO: decrypt key storage
    public void decrypt(){
        System.out.print("Enter the file path: ");
        path = scan.next();
        System.out.print("Enter a key: ");
        String key  = scan.next();
        System.out.print("Enter a key length: ");
        keyLength = scan.nextInt();
        em = new EncryptionManager();
        // call the encryption method
        em.decryptFile(path, getKeyFromPassword(), initializationVector());

    }

    public AES_Encryption() {

    }

    /**
     * create a salt strengthen the generated password
     *
     * @return byte[] salt
     */
    private byte[] createSalt() {
        byte[] salt = new byte[20];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        return salt;
    }

    public IvParameterSpec initializationVector() {

        byte[] Iv = new byte[16];
        try {
            SecureRandom sr = SecureRandom.getInstanceStrong();
            sr.nextBytes(Iv); // fill the array

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error while creating Iv");
        }
        return new IvParameterSpec(Iv);
    }


    /**
     * Print the Iv
     * @param iv
     * @return
     */
    public String iv(IvParameterSpec iv){
        byte[] data = iv.getIV();
        return  Base64.getEncoder().encodeToString(data);
    }

    public SecretKey getKeyFromPassword() {
        SecretKey secretKey = null;
        try {

            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), createSalt(), 65536, keyLength);// create the key specifics
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); // get instance of skf with the encryption algorithm

            secretKey = new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), "AES"); // generate the secret key

        } catch (NoSuchAlgorithmException e) {

        } catch (InvalidKeySpecException e) {

        }
        return secretKey;
    }


}

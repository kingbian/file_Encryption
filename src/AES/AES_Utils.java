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
import java.util.InputMismatchException;
import java.util.Scanner;

public class AES_Utils {
    //private String password;



    private String path;

    Scanner scan = new Scanner(System.in);

    EncryptionManager em;

    public void encrypt(){
        System.out.print("Enter the file path: ");
        path = scan.next();
        System.out.print("Enter a password: ");
        String password = scan.next();
       /* System.out.print("Enter a key length: ");
       int  keyLength = scan.nextInt();*/
        em = new EncryptionManager();
        // call the encryption method
        em.encryptFile(path, password);

    }
    // TODO: decrypt key storage
    public void decrypt(){
        System.out.print("Enter the file path: ");
        path = scan.next();
        System.out.print("Enter a password: ");
        String password  = scan.next();
        System.out.println(password);
        /*System.out.print("Enter a Iv: ");
        String initializeV= scan.next();
        ;*/

        em =  new EncryptionManager();
        // call the encryption method
        em.decryptFile(path, password);

    }

    public AES_Utils() {

    }

    /**
     * create a salt strengthen the generated password
     *
     * @return byte[] salt
     */
    public byte[] createSalt() {
        byte[] salt = new byte[20];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        return salt;
    }

   /* public IvParameterSpec getUserIv(String iv){


    }*/
    public SecretKey getUserKey(String key){
        try {
            byte[] keyArray = Base64.getDecoder().decode(key);
            return new SecretKeySpec(keyArray, "AES");
        }catch(InputMismatchException e){
            System.out.println("Invalid key format: " + e.getMessage());
            return null;
        }
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

    public SecretKey getKeyFromPassword(char[] password, byte[] salt ) {
        SecretKey secretKey = null;
        try {

            KeySpec keySpec = new PBEKeySpec(password,  salt, 65536, 256);// create the key specifics
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); // get instance of skf with the encryption algorithm

            secretKey = new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), "AES"); // generate the secret key

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("Algorithm not "+e.getMessage());
        }
        return secretKey;
    }


}

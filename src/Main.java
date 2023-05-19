import AES.AES_Encryption;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner scan = new Scanner(System.in);
        AES_Encryption aes;

        System.out.println("Welcome to your file encryption Manager!!");
        String[] options = {"Encrypt a file", "decrypt a file", "Exit"};

        boolean run = true;
        while(run){

            int counter =1;
            for( int i =0; i < options.length; i++){
                System.out.printf("%d. $s\n", counter, options[i]);
            }
            System.out.println("select >: ");
            int choice = scan.nextInt();
            switch (choice){
                case 1:
                    System.out.print("Enter the file path: ");
                    String path = scan.next();


            }

        }

    }
}
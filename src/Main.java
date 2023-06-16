import AES.AES_Utils;

import java.security.NoSuchAlgorithmException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner scan = new Scanner(System.in);
        AES_Utils aes;

        System.out.println("Welcome to your file encryption Manager!!");
        String[] options = {"Encrypt a file", "decrypt a file", "Exit"};

        boolean run = true;

        while(run){

            try{
                int counter =1;
                System.out.println("");
                for( int i =0; i < options.length; i++){
                    System.out.printf("%d. %s \n", counter, options[i]);
                    counter++;
                }
                System.out.print("select >: ");
                int choice = scan.nextInt();
                switch (choice){
                    case 1:
                        aes = new AES_Utils();
                        aes.encrypt();
                        break;
                    case 2:
                        aes = new AES_Utils();
                        aes.decrypt();
                        break;
                    case 3:
                        System.out.println("Session ended....");
                        run = false;
                        break;
                    default:
                        System.out.println("Enter a number within the range");
                        break;
                }
            }catch (InputMismatchException e) {
                System.out.println("Enter a number !!\n");
                scan.nextLine();
            }
        }


    }
}
import java.io.Serializable;
import java.util.Scanner;

public class TrueFalse extends Question implements Serializable {

    private static final long serialVersionUID = 1L;
    //Constructor
    public TrueFalse(String prompt) {
        // This passes the prompt up to the Question class
        super(prompt);
    }

    @Override
    public void display() {
        System.out.println(this.prompt);
        System.out.println(" T) True");
        System.out.println(" F) False");
    }

    @Override
    public void takeQuestion() {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        while (true) {
            System.out.print("Enter your answer (T/F): ");
            userInput = scanner.nextLine().trim().toUpperCase();
            if(userInput.equals("T") || userInput.equals("F")) {
                break; // valid entry
            } else{
                //handle invalid entry
                System.out.println("Invalid input. Please enter 'T' or 'F'.");
            }
        }
        Response newResponse = new Response(userInput);
        this.addUserResponse(newResponse);

    }

    @Override
    public void modify() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Current prompt: " + this.prompt);
        System.out.print("Do you wish to modify the prompt? (yes/no): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("yes")) {
            System.out.print("Enter new prompt: ");
            this.prompt = scanner.nextLine();
            System.out.print("Prompt has been updated: ");
        }else {
            System.out.println("Prompt was not modified.");
        }
    }

}
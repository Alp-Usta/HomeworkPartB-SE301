import java.io.Serializable;
import java.util.Scanner;

public class TrueFalse extends Question implements Serializable {

    private static final long serialVersionUID = 1L;

    // Constructor
    public TrueFalse(String prompt, int numResponses) {
        // Pass both up to the Question class
        super(prompt, numResponses);
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

        // Loop for the number of required responses
        for (int i = 0; i < this.numResponses; i++) {
            while (true) {
                System.out.print("Enter your answer " + (i + 1) + " of " + this.numResponses + " (T/F): ");
                userInput = scanner.nextLine().trim().toUpperCase();

                if (userInput.equals("T") || userInput.equals("F")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'T' or 'F'.");
                }
            }
            Response newResponse = new Response(userInput);
            this.addUserResponse(newResponse);
        }
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
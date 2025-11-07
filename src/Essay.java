import java.io.Serializable;
import java.util.Scanner;

public class Essay extends Question implements Serializable {

    private static final long serialVersionUID = 1L;

    // Constructor
    public Essay(String prompt, int numResponses) {
        // Pass both up to the Question class
        super(prompt, numResponses);
    }

    @Override
    public void display() {
        System.out.println(this.prompt);
        System.out.println("(You may enter a long answer)");
    }

    @Override
    public void takeQuestion() {
        Scanner scanner = new Scanner(System.in);

        // Loop for the number of required responses
        for (int i = 0; i < this.numResponses; i++) {
            System.out.println("Enter your essay response " + (i + 1) + " of " + this.numResponses + " (press Enter when done):");
            String userInput = scanner.nextLine();

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
            System.out.println("Prompt has been updated.");
        } else {
            System.out.println("Prompt was not modified.");
        }
    }
}
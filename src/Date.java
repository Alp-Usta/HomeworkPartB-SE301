import java.io.Serializable;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Date extends Question implements Serializable {

    private static final long serialVersionUID = 1L;

    // Constructor now takes numResponses
    public Date(String prompt, int numResponses) {
        // Pass both up to the Question class
        super(prompt, numResponses);
    }

    @Override
    public void display() {
        System.out.println(this.prompt);
        System.out.println("A date should be entered in the following format: YYYY-MM-DD");
    }

    @Override
    public void takeQuestion() {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        // Loop for the number of required responses
        for (int i = 0; i < this.numResponses; i++) {
            while (true) {
                System.out.print("Enter the date " + (i + 1) + " of " + this.numResponses + " (YYYY-MM-DD): ");
                userInput = scanner.nextLine().trim();

                try {
                    // Try to parse the date
                    LocalDate.parse(userInput);
                    break; // If it works, break the inner loop
                } catch (DateTimeParseException e) {
                    // If it fails, print error and repeat inner loop
                    System.out.println("Invalid format. Please use YYYY-MM-DD.");
                }
            }

            // Save the valid response
            Response newResponse = new Response(userInput);
            this.addUserResponse(newResponse);
        }
    }

    @Override
    public void modify() {
        // This is identical to the TrueFalse modify method
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
import java.io.Serializable;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Date extends Question implements Serializable {

    private static final long serialVersionUID = 1L;

    public Date(String prompt) {
        super(prompt);
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

        while (true) {
            System.out.print("Enter the date (YYYY-MM-DD): ");
            userInput = scanner.nextLine().trim();

            try {
                LocalDate.parse(userInput);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please use YYYY-MM-DD.");
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
            System.out.println("Prompt has been updated.");
        } else {
            System.out.println("Prompt was not modified.");
        }
    }
}
import java.io.Serializable;
import java.util.ArrayList;

public class Survey implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Question> questions;
    private String title;

    //Constructor
    public Survey(String title) {
        this.title = title;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question q) {
        this.questions.add(q);
    }

    //Getter for the title
    public String getTitle() {
        return this.title;
    }

    //Getter for a specific question
    public Question getQuestion(int index) {
        return this.questions.get(index);
    }

    public void display() {
        System.out.println("----- SURVEY: " + this.title + " -----");
        for (int i = 0; i < this.questions.size(); i++) {
            System.out.print("Q" + (i + 1) + ": ");
            this.questions.get(i).display();
            System.out.println(); // Add a space
        }
        System.out.println("---------------------------------");
    }

    public void take() {
        System.out.println("----- TAKING SURVEY: " + this.title + " -----");
        for (int i = 0; i < this.questions.size(); i++) {
            System.out.print("Q" + (i + 1) + ": ");
            this.questions.get(i).display();
            this.questions.get(i).takeQuestion(); // Ask for the answer
            System.out.println(); // Add a space
        }
        System.out.println("----- SURVEY COMPLETE -----");
    }

    public void tabulate() {
        System.out.println("----- TABULATED RESPONSES for " + this.title + " -----");
        for (Question q : this.questions) {
            System.out.println("Question: " + q.getPrompt());
            ArrayList<Response> responses = q.getResponses();

            if (responses.isEmpty()) {
                System.out.println("  (No responses for this question)");
            } else {
                System.out.println("  Responses:");
                for (Response res : responses) {
                    System.out.print("  - ");
                    res.display();
                }
            }
            System.out.println(); // blank line
        }
        System.out.println("----- END OF TABULATION -----");
    }

}
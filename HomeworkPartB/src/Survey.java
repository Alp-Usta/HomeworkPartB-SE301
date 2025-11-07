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

    //Getter
    public String getTitle() {
        return this.title;
    }
    public Question getQuestion(int index) {
        return this.questions.get(index);
    }

    public void display() {
        System.out.println("Survey: " + this.title);
        for (int i = 0; i < this.questions.size(); i++) {
            System.out.print("Q" + (i + 1) + ": ");
            this.questions.get(i).display();
        }
    }

    public void take() {
        System.out.println("Taking Survey: " + this.title);
        for (int i = 0; i < this.questions.size(); i++) {
            System.out.print("Q" + (i + 1) + ": ");
            this.questions.get(i).display();
            //user's answer
            this.questions.get(i).takeQuestion();

            System.out.println();
        }
        System.out.println("----- SURVEY COMPLETE -----");
    }

    public void tabulate() {
        System.out.println("----- TABULATED RESPONSES for " + this.title + " -----");
        for (Question q : this.questions) {
            System.out.println("Question: " + q.getPrompt());
            ArrayList<Response> response = q.getResponses();

            if (response.isEmpty()) {
                System.out.println("  (No responses for this question)");
            } else {
                System.out.println("  Response:");
                for (Response res : response) {
                    System.out.print("  - ");
                    //Tell the Response object to display itself
                    res.display();
                }
            }
            System.out.println(); // blank line
        }
        System.out.println("----- END OF TABULATION -----");
    }

    public void modify() {
        System.out.println("Modify method not implemented yet.");
    }

}
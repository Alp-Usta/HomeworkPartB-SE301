import java.io.Serializable;

public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private String responseText;

    public Response(String responseText) {
        this.responseText = responseText;
    }

    public String getResponseText() {
        return responseText;
    }

    public void display() {
        System.out.println(responseText);
    }
}
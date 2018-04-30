import java.util.List;

public class Mail {
    private String from;
    private List<String> to;
    private String subject;
    private String data;

    public Mail(String from, List<String> to, String subject, String data){
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.data = data;
    }

    public String getFrom(){
        return from;
    }

    public List<String> getTo(){
        return to;
    }

    public String getSubject(){
        return subject;
    }

    public String getData(){
        return data;
    }
}

public class Personne {
    private String name;
    private String email;

    public Personne(String name){
        this.name = name;
    }

    public Personne(String name, String email){
        this.name = name;
        this.email = email;
    }

    public Personne(){}

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }
}

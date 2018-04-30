import java.util.List;

public class Groupe {
    private List<Personne> groupe;

    public Groupe(List<Personne> groupe){
        this.groupe = groupe;
    }

    public boolean addPersonne(Personne personne){
        if(groupe.contains(personne)){
            return false;
        }
        groupe.add(personne);
        return true;
    }

    public List<Personne> getGroupe(){
        return groupe;
    }
}
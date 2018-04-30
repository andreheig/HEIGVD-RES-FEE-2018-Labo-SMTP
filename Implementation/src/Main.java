//import config.properties;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    final static int BUFFER_SIZE = 1024;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}", Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) {

        String server = "localhost";
        int port = 25;
        int textMail = 0;
        int numberOfGroup = 0;
        int numberOfVictim = 3;
        String subject = "";
        String ehloname = "";
        List<Personne> peopleList = new ArrayList<>();
        List<String> mailList = new ArrayList<>();
        List<Mail> spams = new ArrayList<>();
        smtpClient client = null;

        // Lecture du fichier de config, et assignation des différentes valeurs
        try (InputStream in = new FileInputStream("Implementation/src/config.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            for (String property : prop.stringPropertyNames()) {
                String value = prop.getProperty(property);
                if (property.equalsIgnoreCase("port")) {
                    port = Integer.parseInt(value);
                }
                if (property.equalsIgnoreCase("smtpserver")) {
                    server = value;
                }
                if (property.equalsIgnoreCase("numberofgroup")) {
                    numberOfGroup = Integer.parseInt(value);
                    if (numberOfGroup == 0) {
                        System.out.println("nombre de groupe défini automatiquement");
                    }
                }
                if (property.equalsIgnoreCase("numberofvictim")) {
                    numberOfVictim = Integer.parseInt(value);
                    if (numberOfVictim == 0) {
                        System.out.println("nombre de victime défini automatiquement");
                    } else if (numberOfVictim < 3) {
                        System.out.println("nombre de victime pas en adéquation avec la donnée, mis au minimum");
                        numberOfVictim = 3;
                    }
                }
                if (property.equalsIgnoreCase("mail")) {
                    textMail = Integer.parseInt(value);
                }
                if (property.equalsIgnoreCase("subject")) {
                    subject = value;
                }
                if(property.equalsIgnoreCase("name")){
                    ehloname = value;
                }
                in.close();
                //System.out.println(property + "=" + value);
            }
        } catch (IOException e) {

            e.printStackTrace();

        }

        // Lecture du fichier de personnes et remplissage de la liste
        try (InputStream peoples = new FileInputStream("Implementation/src/testlist")) {
            ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int newBytes;
            String[] tmp = new String[BUFFER_SIZE];
            String email;
            while ((newBytes = peoples.read(buffer)) != -1) {
                responseBuffer.write(buffer, 0, newBytes);
                tmp = responseBuffer.toString().split("\n");
            }
            // Permet de peupler notre liste
            for (int i = 0; i < tmp.length; ++i) {
                // Teste que l'email est correct dans le cas ou le fichiers d'email contienne des erreurs
                if(!validate(tmp[i])){
                    System.err.println("email " + tmp[i] + " non valide!");
                    break;
                }
                // Permet d'avoir prenom et nom
                email = tmp[i].substring(tmp[i].indexOf('"') + 1, tmp[i].indexOf('"', tmp[i].indexOf('"') + 1));
                // Permet d'avoir l'email
                email.replace("\r", "").replace("\n", "");
                peopleList.add(new Personne("", email));
            }

            // Permet de controler que la liste est au complet
            for (int i = 0; i < peopleList.size(); ++i) {
                //System.out.println(peopleList.get(i).getName() + " " + peopleList.get(i).getEmail());
            }
            peoples.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

        // Récupérations des mails différents
        try (InputStream mails = new FileInputStream("Implementation/src/MailType")) {
            ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int newBytes;
            String[] tmp = new String[BUFFER_SIZE];
            String data;
            while ((newBytes = mails.read(buffer)) != -1) {
                responseBuffer.write(buffer, 0, newBytes);
                //System.out.println(responseBuffer);
                tmp = responseBuffer.toString().split("¬¬");
                //System.out.println(tmp.length);
            }

            // Permet de peupler notre liste
            for (int i = 0; i < tmp.length; ++i) {
                mailList.add(new String(tmp[i]));
            }

            // Permet de controler que la liste est au complet
            for (int i = 0; i < mailList.size(); ++i) {
                //System.out.println(mailList.get(i));
            }
            mails.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
        if (textMail > mailList.size()) {
            System.err.println("Numéro de message pas en adéquation avec le nombre de mails type");
            throw new IllegalArgumentException();
        }

        // Test des valeurs si le nombres de groupes spécifié, avec le nombre de victimes peut coller avec nos données
        if (numberOfGroup == 0) {

            // Permet d'avoir un nombre raisonnable de groupe ni trop grand ni trop petit
            int maximumGroup = (int) (peopleList.size() / (double) 3);
            System.out.println(maximumGroup);
            if (maximumGroup < 1) {
                System.err.println("Nombre d'email insuffisant pour créer un groupe!");
                throw new IllegalArgumentException();
            } else if (maximumGroup <= 2) {
                numberOfGroup = maximumGroup;
            } else {
                int minLength = peopleList.size();
                List<Integer> divisor = new ArrayList<>();
                for (int i = maximumGroup; i >= 2; --i) {
                    System.out.println("Division " + peopleList.size() / (double) i);
                    if (Double.toString(peopleList.size() / (double) i).length() <= minLength && (peopleList.size() / (double) i) < maximumGroup) {
                        if (Double.toString(peopleList.size() / (double) i).length() < minLength) {
                            divisor.clear();
                        }
                        minLength = Double.toString(peopleList.size() / (double) i).length();
                        System.out.println("min length : " + minLength);
                        divisor.add((int) (peopleList.size() / (double) i));
                    }
                }

                for (int j = 0; j < divisor.size(); ++j) {
                    System.out.println("divisor : " + divisor.get(j));
                    System.out.println("nombre de groupe : " + peopleList.size() / divisor.get(j));
                }
                if (divisor.size() < 2) {
                    numberOfGroup = divisor.get(0);
                } else {
                    numberOfGroup = divisor.get(divisor.size() / 2);
                }
            }
        }
        System.out.println("nombre de group " + numberOfGroup);

        if (numberOfVictim == 0) {
            if(numberOfGroup ==1 ){
                numberOfVictim = peopleList.size();
            }
            else {
                numberOfVictim = peopleList.size() / numberOfGroup;
            }
            System.out.println("nombre de victimes " + numberOfVictim);
        }
        if(numberOfVictim < 3){
            System.err.println("Nombre de victime pas assez suffisant");
            throw new IllegalArgumentException();
        }
        if(numberOfVictim * numberOfGroup > peopleList.size()){
            System.err.println("Erreur de nombre de groupe et de victime dans le fichier de config");
            throw new IllegalArgumentException();
        }

        // Permet de créer les "Spams"
        Personne tmp = new Personne();
        Mail spam = new Mail("", new ArrayList<>(), "", "");
        String from = "";

        for (int i = 0; i < numberOfGroup; ++i) {
            List<String> to = new ArrayList<>();
            for (int j = 0; j < numberOfVictim; ++j) {
                tmp = new Personne(peopleList.get((i * numberOfVictim) + j).getName(), peopleList.get((i * numberOfVictim) + j).getEmail());
                if (j == 0) {
                    from = tmp.getEmail();
                    from.replace("\r", "").replace("\n", "");
                } else {
                    to.add(tmp.getEmail().replace("\r", "").replace("\n", ""));
                }
            }
            spams.add(new Mail(from, to, subject, mailList.get(textMail - 1)));
        }
        try {
                client = new smtpClient(server, port, ehloname);
                client.connect();

                System.out.println("Création Spams");
                for (int k = 0; k < spams.size(); ++k) {
                    client.buildMail(spams.get(k));
                }
                client.disconnect();
                client = null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
            if(client != null){
                try {
                    client.closeStream();
                }
                catch (IOException ioExcept){
                    System.err.println("Erreur fermeture sur finally");
                }
            }
        }
        }


    public static boolean validate(String emailStr) {
        Matcher matcher = null;
        try {
            matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        }
        catch (NullPointerException nullptr){
            System.err.println("email inexistant");
            return false;
        }
        return matcher.find();
    }

    }

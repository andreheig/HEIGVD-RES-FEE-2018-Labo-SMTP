import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class smtpClient {
    private Socket client = null;
    private String serverName;
    private int portServer;
    private String ehloName;


    protected OutputStream os = null;
    protected InputStream is = null;
    private boolean connected = false;
    protected ByteArrayOutputStream responseBuffer;
    protected byte[] buffer;
    protected int newBytes;

    final static int BUFFER_SIZE = 1024;

    public smtpClient(String server, int port, String name){
        serverName = server;
        portServer = port;
        ehloName = name;
    }

    // Permet de se connecter au serveur
    public void connect() throws IOException{

            client = new Socket(serverName, portServer);
            os = client.getOutputStream();
            is = client.getInputStream();
            connected = true;
            buffer = new byte[BUFFER_SIZE];
            responseBuffer = new ByteArrayOutputStream();
            newBytes = is.read(buffer);
            responseBuffer.write(buffer, 0, newBytes);
            responseBuffer.reset();

    }

    // Permet de s'annoncer au serveur
    public void saidEHLO(String name) throws IOException{
        responseBuffer.reset();
        os.write((smtpProtocol.CMD_EHLO + " " + name + smtpProtocol.separator).getBytes());
        os.flush();
        newBytes = is.read(buffer);
        responseBuffer.write(buffer, 0, newBytes);
        if(responseBuffer.toString().contains(smtpProtocol.CORRECT)){
            System.out.println("EHLO OK");
        }
        // Chaines à tester: 500, 501, 502, 550 => erreur
        else if(responseBuffer.toString().contains(smtpProtocol.ERR_SYNTAXE_OR_CMD) ||
                responseBuffer.toString().contains(smtpProtocol.ERR_SYNTAXE_PARAM_OR_ARG) ||
                responseBuffer.toString().contains(smtpProtocol.ERR_CMD_NOT_IMPL) ||
                responseBuffer.toString().contains(smtpProtocol.ERR_SEND_OR_SMTP_RELAY)){
            System.err.println("Erreur EHLO COMMANDE");
        }
        responseBuffer.reset();
    }

    // Permet de construire un mail
    public void buildMail(Mail mail) throws IOException{
        responseBuffer.reset();
        saidEHLO(ehloName);
        //System.out.println("from = " + mail.getFrom());
        setFROM(mail.getFrom());

        for(int i = 0; i < mail.getTo().size(); ++i) {
            System.out.print("BuildMail To: " + mail.getTo().get(i));
            setRCPT(mail.getTo().get(i));
        }
        System.out.println("");

        os.write((smtpProtocol.CMD_DATA + smtpProtocol.separator).getBytes());
        os.flush();
        newBytes = is.read(buffer);
        responseBuffer.write(buffer, 0, newBytes);
        if(responseBuffer.toString().contains(smtpProtocol.START_WRITE_MAIL)){
            os.write(("From: " + mail.getFrom() + smtpProtocol.separator + "To: " + mail.getTo() + smtpProtocol.separator +
                    "Subject: " + mail.getSubject() + smtpProtocol.separator + mail.getData() + smtpProtocol.separator +
                    smtpProtocol.CMD_END_DATA).getBytes());
            os.flush();
            responseBuffer.reset();
            newBytes = is.read(buffer);
            responseBuffer.write(buffer, 0, newBytes);
            //System.out.println("end data " + responseBuffer);
            if(responseBuffer.toString().contains(smtpProtocol.CORRECT)){
                System.out.println("Mail send OK");
            }
            else{
                System.err.println("Error sending mail");
            }
        }
        else{
            System.err.println("Erreur DATA");
        }

        responseBuffer.reset();
    }

    // Permet de setter l'emmetteur
    private void setFROM(String from) throws IOException{
        responseBuffer.reset();
        System.out.println("debut from");
        System.out.println((smtpProtocol.CMD_MAIL + from + smtpProtocol.separator));
        os.write((smtpProtocol.CMD_MAIL + from + smtpProtocol.separator).getBytes());
        //os.write((smtpProtocol.CMD_EHLO + " " + "coucou" + smtpProtocol.separator).getBytes());
        os.flush();
        newBytes = is.read(buffer);
        responseBuffer.write(buffer, 0, newBytes);
        if(responseBuffer.toString().contains(smtpProtocol.CORRECT)){
            System.out.println("FROM TO: OK");
        }
        else{
            System.err.println("Erreur FROM TO:");
        }
        responseBuffer.reset();
    }

    // Permet de setter un destinataire
    private void setRCPT(String rcpt) throws IOException{
        responseBuffer.reset();
        os.write((smtpProtocol.CMD_RCPT + rcpt + smtpProtocol.separator).getBytes());
        os.flush();
        newBytes = is.read(buffer);
        responseBuffer.write(buffer, 0, newBytes);
        if(responseBuffer.toString().contains(smtpProtocol.CORRECT)){
            System.out.print("RCPT TO: OK ");
        }
        else{
            System.err.print("Erreur RCPT TO: ");
        }
        responseBuffer.reset();
    }

    // Permet de lancer la déconnection, ainsi que la fermeture des flux
    public void disconnect() throws IOException {
        responseBuffer.reset();
        os.write((smtpProtocol.CMD_QUIT + smtpProtocol.separator).getBytes());
        os.flush();
        newBytes = is.read(buffer);
        responseBuffer.write(buffer, 0, newBytes);
        closeStream();
        connected = false;
    }

    // Permet de fermer les flux dans le cas de problème IO
    public void closeStream() throws IOException{
        os.close();
        is.close();
        client.close();
    }
}

import com.sun.corba.se.impl.ior.TaggedProfileTemplateFactoryFinderImpl;

public class smtpProtocol {
    public final static String VERSION = "1.0";

    public final static int DEFAULT_PORT = 25;

    public final static String separator = System.getProperty("line.separator");

    // Commandes de basses devant obligatoirement être supporter pour discuter avec le serveur mail
    public final static String CMD_EHLO = "EHLO";
    public final static String CMD_MAIL = "MAIL FROM: ";
    public final static String CMD_RCPT = "RCPT TO: ";
    public final static String CMD_DATA = "DATA";
    public final static String CMD_END_DATA = separator + "." + separator;
    public final static String CMD_QUIT = "QUIT";


    // Commandes étendues implémenté plus tard, si on a le temps
    public final static String CMD_HELO = "HELO";
    public final static String CMD_RSET = "RSET";
    public final static String CMD_NOOP = "NOOP";
    public final static String CMD_VRFY = "VRFY";

    // Codes différents
    public final static String ERR_SYNTAXE_OR_CMD = "500"; // 500  Syntax error, command unrecognized (This may include errors such as command line too long)
    public final static String ERR_SYNTAXE_PARAM_OR_ARG = "501"; // 501  Syntax error in parameters or arguments
    public final static String ERR_CMD_NOT_IMPL = "502"; // 502  Command not implemented (see Section 4.2.4)
    public final static String ERR_BAD_SEQUENCE_CMD = "503";// 503  Bad sequence of commands
    public final static String ERR_CMD_PARAM_NOT_IMPLEMENTED = "504"; // 504  Command parameter not implemented
    public final static String ERR_CMD_MAIL_FROM_OR_RCPT_TO = "555"; // 555  MAIL FROM/RCPT TO parameters not recognized or not implemented
    public final static String ERR_SEND_OR_SMTP_RELAY = "550"; // 550  Requested action not taken: mailbox unavailable (e.g., mailbox not found, no access, or command rejected for policy reasons)

    public final static String CORRECT = "250"; // 250  Requested mail action okay, completed
    public final static String START_WRITE_MAIL = "354"; // 354  Start mail input; end with <CRLF>.<CRLF>

    /*

   211  System status, or system help reply

   214  Help message (Information on how to use the receiver or the
      meaning of a particular non-standard command; this reply is useful
      only to the human user)

      220  <domain> Service ready

   221  <domain> Service closing transmission channel

   421  <domain> Service not available, closing transmission channel
      (This may be a reply to any command if the service knows it must
      shut down)


   251  User not local; will forward to <forward-path> (See Section 3.4)

   252  Cannot VRFY user, but will accept message and attempt delivery
      (See Section 3.5.3)

   455  Server unable to accommodate parameters

   450  Requested mail action not taken: mailbox unavailable (e.g.,
      mailbox busy or temporarily blocked for policy reasons)


   451  Requested action aborted: error in processing

   551  User not local; please try <forward-path> (See Section 3.4)

   452  Requested action not taken: insufficient system storage

   552  Requested mail action aborted: exceeded storage allocation

   553  Requested action not taken: mailbox name not allowed (e.g.,
      mailbox syntax incorrect)

   554  Transaction failed (Or, in the case of a connection-opening
      response, "No SMTP service here")

*/
    //public final static String[] SUPPORTED_COMMANDS = new String[]{CMD_HELP, CMD_RANDOM, CMD_LOAD, CMD_INFO, CMD_BYE};
}

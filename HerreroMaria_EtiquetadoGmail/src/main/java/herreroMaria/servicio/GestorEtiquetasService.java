package herreroMaria.servicio;

import herreroMaria.modelo.Correo;

import javax.mail.*;
import java.util.Properties;

public class GestorEtiquetasService {

    private final Store store;
    private final Folder inbox;

    public GestorEtiquetasService(String host, String username, String password) throws MessagingException {
        System.out.println("Conectando a " + host + " como " + username);

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", host);
        properties.put("mail.imaps.port", "993");

        Session session = Session.getDefaultInstance(properties, null);
        store = session.getStore("imaps");
        store.connect(host, username, password);

        inbox = store.getDefaultFolder().getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);
        System.out.println("Conexión exitosa. Bandeja de entrada abierta.");
    }

    public void etiquetarCorreos() throws MessagingException {
        System.out.println("Recuperando correos...");
        Message[] messages = inbox.getMessages();
        System.out.println("Total de correos: " + messages.length);

        for (int i = 0; i < messages.length; i++) {
            Message message = messages[i];

            Correo correo = new Correo(
                    message.getSubject()
            );

            System.out.println("Email " + (i + 1) + ":");
            System.out.println("Asunto: " + correo.getAsunto());

            String folder = determinarEtiqueta(i);

            if (folder != null) {
                moverCorreo(message, folder);
                System.out.println("Correo " + (i + 1) + " movido a '" + folder + "'");
            } else {
                System.out.println("Correo " + (i + 1) + " sin etiqueta asignada.");
            }
        }

        inbox.expunge();
        System.out.println("Proceso de etiquetado completado.");
    }

    private String determinarEtiqueta(int index) {
        if (index < 3) return "Done";
        if (index == 3) return "Work.in.Progress";
        if (index < 7) return "To.be.Done";
        return null;
    }

    private void moverCorreo(Message message, String folderName) throws MessagingException {
        Folder folder = store.getFolder(folderName);
        if (!folder.exists()) {
            folder.create(Folder.HOLDS_MESSAGES);
            System.out.println("Carpeta '" + folderName + "' creada.");
        }

        folder.open(Folder.READ_WRITE);
        inbox.copyMessages(new Message[]{message}, folder);
        message.setFlag(Flags.Flag.DELETED, true);
        folder.close(false);
    }

    public void cerrar() throws MessagingException {
        if (inbox != null && inbox.isOpen()) {
            inbox.close(false);
        }
        if (store != null) {
            store.close();
        }
        System.out.println("Conexión cerrada.");
    }
}
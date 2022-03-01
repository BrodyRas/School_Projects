import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class Main {
    public static void main(String[] args) throws IOException {
        ContactManager contactManager = new ContactManager();
        contactManager.addContact(new Contact("first","last","1234","a@b.c"));
        contactManager.addContact(new Contact("brody","ras","768-3813","brody@ras.com"));
        contactManager.addContact(new Contact("michael","black","696-9696","san@negrit.o"));

        ContactManagerAdapter adapter = new ContactManagerAdapter(contactManager);

        Writer writer = new PrintWriter(System.out, true);
        Table table = new Table(writer, adapter);

        table.display();
        writer.flush();
    }
}

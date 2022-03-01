public class ContactManagerAdapter implements TableData {
    private ContactManager contactManager;

    public ContactManagerAdapter(ContactManager contactManager) {
        this.contactManager = contactManager;
    }

    // TABLE STRUCTURE

    // Returns number of columns in the table
    @Override
    public int getColumnCount() {
        return 4;
    }

    // Returns number of rows in the table
    @Override
    public int getRowCount() {
        return contactManager.getContactCount();
    }

    // Returns the number of spaces between the table columns
    @Override
    public int getColumnSpacing() {
        return 1;
    }

    // Returns the number of empty lines between the table rows
    @Override
    public int getRowSpacing() {
        return 1;
    }

    // Returns the character to be used to underline the table headers
    @Override
    public char getHeaderUnderline() {
        return '*';
    }

    // COLUMN STRUCTURE

    // Returns the header string for the specified column
    @Override
    public String getColumnHeader(int col) {
        switch (col){
            case 0:
                return "FIRST NAME";
            case 1:
                return "LAST NAME";
            case 2:
                return "PHONE #";
            case 3:
                return "EMAIL";
            default:
                return "BAD INDEX";
        }
    }

    // Returns the width (in spaces) of the specified column
    @Override
    public int getColumnWidth(int col) {
        return 20;
    }

    // Returns the justification for the values in the specified column
    @Override
    public Justification getColumnJustification(int col) {
        return Justification.Left;
    }

    // CELL VALUES

    // Returns the value in the specified table cell
    @Override
    public String getCellValue(int row, int col) {
        Contact contact = contactManager.getContact(row);
        switch (col){
            case 0:
                return contact.getFirstName();
            case 1:
                return contact.getLastName();
            case 2:
                return contact.getPhone();
            case 3:
                return contact.getEmail();
            default:
                return "BAD INDEX";
        }
    }
}

public class Open implements ICommand {
    public Open(IDocument _document, String openFileName) {
        this._document = _document;
        this.openFileName = openFileName;
    }

    private IDocument _document;
    private String openFileName, sequence;

    @Override
    public void excecute() {
        sequence = _document.sequence().toString();
        _document.open(openFileName);
    }

    @Override
    public void undo() {
        _document.clear();
        _document.insert(0, sequence);
    }
}

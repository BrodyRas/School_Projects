public class Start implements ICommand {
    IDocument _document;
    String sequence;

    public Start(IDocument _document) {
        this._document = _document;
        sequence = _document.sequence().toString();
    }

    @Override
    public void excecute() {
        _document.clear();
    }

    @Override
    public void undo() {
        _document.insert(0,sequence);
    }
}

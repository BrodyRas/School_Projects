public class Replace implements ICommand {
    IDocument _document;
    int replaceIndex, replaceDistance;
    String replacementString;
    String deleted;

    public Replace(IDocument _document, int replaceIndex, int replaceDistance, String replacementString) {
        this._document = _document;
        this.replaceIndex = replaceIndex;
        this.replaceDistance = replaceDistance;
        this.replacementString = replacementString;
    }

    @Override
    public void excecute() {
        deleted = _document.delete(replaceIndex, replaceDistance);
        _document.insert(replaceIndex, replacementString);
    }

    @Override
    public void undo() {
        _document.delete(replaceIndex, replaceDistance);
        _document.insert(replaceIndex, deleted);
    }
}

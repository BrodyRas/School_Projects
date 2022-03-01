public class Delete implements ICommand {
    IDocument _document;
    int deletionDistance, deletionIndex;
    String deleted;

    public Delete(IDocument _document, int deletionDistance, int deletionIndex) {
        this._document = _document;
        this.deletionDistance = deletionDistance;
        this.deletionIndex = deletionIndex;
    }

    @Override
    public void excecute() {
        if (deletionDistance != -1) {
            deleted = _document.delete(deletionIndex, deletionDistance);
            if (deleted == null) {
                System.out.println("Deletion unsuccessful");
            }
        }
    }

    @Override
    public void undo() {
        _document.insert(deletionIndex, deleted);
    }
}

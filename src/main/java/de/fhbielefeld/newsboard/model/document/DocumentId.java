package de.fhbielefeld.newsboard.model.document;

public class DocumentId {

    final static DocumentId NONE = new DocumentId(-1);

    private final int id;

    public DocumentId(int id) {
        this.id = id;
    }

    public int raw() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocumentId that = (DocumentId) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "DocumentId{" +
                "id=" + id +
                '}';
    }
}

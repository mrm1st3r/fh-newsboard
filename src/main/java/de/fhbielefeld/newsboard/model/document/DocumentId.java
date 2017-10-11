package de.fhbielefeld.newsboard.model.document;

import de.fhbielefeld.newsboard.model.ValueObject;
import lombok.Data;

@Data
public class DocumentId implements ValueObject {

    static final DocumentId NONE = new DocumentId(-1);

    private final int id;

    public int raw() {
        return id;
    }
}

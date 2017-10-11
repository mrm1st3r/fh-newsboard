package de.fhbielefeld.newsboard.model;

import de.fhbielefeld.newsboard.model.module.ModuleId;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Domain class representing external documents, which may not be classified.
 *
 * @author Felix Meyer
 */
@Data
@AllArgsConstructor
public class ExternalDocument implements Aggregate<ExternalDocument> {
    private int id;
    private String title;
    private String html;
    private final ModuleId externalModule;

    public void setId(int id) {
        if (this.id != -1) {
            throw new IllegalStateException("External document has already an ID assigned");
        }
        this.id = id;
    }
}

package de.fhbielefeld.newsboard.model.module;

import de.fhbielefeld.newsboard.model.Aggregate;
import de.fhbielefeld.newsboard.model.access.AccessId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Domain class representing external modules like crawler and classifiers, which are not directly part of the newsboard.
 *
 * @author Felix Meyer
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(of={"id"})
public class ExternalModule implements Aggregate<ExternalModule> {
    private final ModuleId id;
    private String name;
    private String author;
    private String description;
    private final AccessId accessId;
}

package jig.domain.model.tag;

import jig.domain.model.thing.Names;

public interface TagRepository {

    void register(ThingTag thingTag);

    Names find(Tag tag);
}

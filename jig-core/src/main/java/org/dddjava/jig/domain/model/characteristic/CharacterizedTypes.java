package org.dddjava.jig.domain.model.characteristic;

import org.dddjava.jig.domain.model.implementation.bytecode.ByteCode;
import org.dddjava.jig.domain.model.implementation.bytecode.ByteCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * 特徴付けられた型一覧
 */
public class CharacterizedTypes {

    List<CharacterizedType> list;

    public CharacterizedTypes(List<CharacterizedType> list) {
        this.list = list;
    }

    public CharacterizedTypes(ByteCodes byteCodes, CharacterizedTypeFactory characterizedTypeFactory) {
        this(new ArrayList<>());

        for (ByteCode byteCode : byteCodes.list()) {
            CharacterizedType characterizedType = characterizedTypeFactory.create(byteCode);
            list.add(characterizedType);
        }
    }

    public CharacterizedTypeStream stream() {
        return new CharacterizedTypeStream(list.stream());
    }
}

package jig.infrastructure.asm;

import jig.domain.model.characteristic.Characteristic;
import jig.domain.model.characteristic.CharacteristicRepository;
import jig.domain.model.identifier.Identifier;
import jig.domain.model.relation.RelationRepository;
import jig.domain.model.relation.RelationType;
import jig.domain.model.relation.Relations;
import jig.infrastructure.JigPaths;
import jig.infrastructure.RecursiveFileVisitor;
import jig.infrastructure.onmemoryrepository.OnMemoryCharacteristicRepository;
import jig.infrastructure.onmemoryrepository.OnMemoryRelationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class AsmClassFileReaderTest {

    static CharacteristicRepository characteristicRepository = new OnMemoryCharacteristicRepository();
    static RelationRepository relationRepository = new OnMemoryRelationRepository();

    @BeforeAll
    static void before() {

        Path path = Paths.get("../sut/build/classes/java/main");

        AsmClassFileReader analyzer = new AsmClassFileReader(characteristicRepository, relationRepository, new JigPaths());
        RecursiveFileVisitor recursiveFileVisitor = new RecursiveFileVisitor(analyzer::execute);
        recursiveFileVisitor.visitAllDirectories(path);
    }

    @Test
    void 関連() {
        Relations datasources = relationRepository.findTo(new Identifier("sut.domain.model.hoge.HogeRepository"), RelationType.IMPLEMENT);
        assertThat(datasources.list()).isNotEmpty();

        Relations method = relationRepository.findTo(new Identifier("sut.domain.model.hoge.HogeRepository.all()"), RelationType.IMPLEMENT);
        assertThat(method.list()).isNotEmpty();
    }

    @Test
    void タグ() {
        assertThat(characteristicRepository.find(Characteristic.SERVICE).list()).extracting(Identifier::value)
                .containsExactlyInAnyOrder(
                        "sut.application.service.CanonicalService",
                        "sut.application.service.ThrowsUnknownExceptionService");

        assertThat(characteristicRepository.find(Characteristic.ENUM).list()).hasSize(4);
        assertThat(characteristicRepository.find(Characteristic.ENUM_BEHAVIOUR).list()).extracting(Identifier::value)
                .containsExactly("sut.domain.model.kind.BehaviourEnum");
        assertThat(characteristicRepository.find(Characteristic.ENUM_PARAMETERIZED).list()).extracting(Identifier::value)
                .containsExactly("sut.domain.model.kind.ParameterizedEnum");
        assertThat(characteristicRepository.find(Characteristic.ENUM_POLYMORPHISM).list()).extracting(Identifier::value)
                .containsExactly("sut.domain.model.kind.PolymorphismEnum");

        assertThat(characteristicRepository.find(Characteristic.IDENTIFIER).list()).extracting(Identifier::value)
                .containsExactlyInAnyOrder(
                        "sut.domain.model.fuga.FugaIdentifier",
                        "sut.domain.model.fuga.FugaName");

        assertThat(characteristicRepository.find(Characteristic.NUMBER).list()).extracting(Identifier::value)
                .containsExactly("sut.domain.model.hoge.hogera.HogeraAmount");

        assertThat(characteristicRepository.find(Characteristic.DATE).list()).extracting(Identifier::value)
                .containsExactly("sut.domain.model.hoge.hogera.HogeraDate");

        assertThat(characteristicRepository.find(Characteristic.TERM).list()).extracting(Identifier::value)
                .containsExactly("sut.domain.model.hoge.hogera.HogeraTerm");

        assertThat(characteristicRepository.find(Characteristic.COLLECTION).list()).extracting(Identifier::value)
                .containsExactly("sut.domain.model.hoge.Hoges");
    }

}
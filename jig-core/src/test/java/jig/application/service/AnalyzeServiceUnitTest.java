package jig.application.service;

import jig.domain.model.project.ProjectLocation;
import jig.domain.model.specification.ModelReader;
import jig.infrastructure.JigPaths;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class AnalyzeServiceUnitTest {

    @Test
    void test() {
        JigPaths jigPaths = new JigPaths(
                "not/match/any/directory",
                "not/match/any/directory",
                "not/match/any/directory");
        ModelReader modelReaderMock = mock(ModelReader.class);

        AnalyzeService sut = new AnalyzeService(modelReaderMock, null, null, null, jigPaths, null);

        ProjectLocation location = new ProjectLocation(Paths.get(""));

        assertThatThrownBy(() -> sut.importProject(location))
                .isInstanceOf(RuntimeException.class);
        verifyZeroInteractions(modelReaderMock);
    }
}

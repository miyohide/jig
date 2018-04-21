package jig.application.usecase;

import jig.application.service.DatasourceService;
import jig.application.service.DependencyService;
import jig.application.service.GlossaryService;
import jig.application.service.SpecificationService;
import jig.domain.model.project.ProjectLocation;
import jig.domain.model.relation.dependency.PackageDependencies;
import jig.domain.model.specification.SpecificationSources;
import jig.domain.model.specification.Specifications;
import jig.infrastructure.JigPaths;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeService {

    final JigPaths jigPaths;
    final SpecificationService specificationService;
    final DependencyService dependencyService;
    final GlossaryService glossaryService;
    final DatasourceService datasourceService;

    public AnalyzeService(JigPaths jigPaths,
                          SpecificationService specificationService,
                          DependencyService dependencyService,
                          GlossaryService glossaryService,
                          DatasourceService datasourceService) {
        this.specificationService = specificationService;
        this.dependencyService = dependencyService;
        this.jigPaths = jigPaths;
        this.glossaryService = glossaryService;
        this.datasourceService = datasourceService;
    }

    public PackageDependencies packageDependencies(ProjectLocation projectLocation) {
        importSpecification(projectLocation);
        glossaryService.importJapanese(jigPaths.getPackageNameSources(projectLocation));
        return dependencyService.packageDependencies();
    }

    public void importProject(ProjectLocation projectLocation) {
        importSpecification(projectLocation);
        datasourceService.importDatabaseAccess(jigPaths.getSqlSources(projectLocation));
        glossaryService.importJapanese(jigPaths.getTypeNameSources(projectLocation));
    }

    public void importSpecification(ProjectLocation projectLocation) {
        SpecificationSources specificationSources = jigPaths.getSpecificationSources(projectLocation);
        Specifications specifications = specificationService.specification(specificationSources);

        dependencyService.registerSpecifications(specifications);
    }
}
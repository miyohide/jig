package org.dddjava.jig.domain.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 取り扱うドキュメントの種類
 */
public enum DocumentType {
    ServiceMethodCallHierarchy("jig-diagram_service-method-call-hierarchy.png"),
    PackageDependency("jig-diagram_package-dependency.png"),
    ApplicationList("jig-list_application.xlsx"),
    DomainList("jig-list_domain.xlsx"),
    EnumUsage("jig-diagram_enum-usage.png");

    private final String documentFileName;

    DocumentType(String documentFileName) {
        this.documentFileName = documentFileName;
    }

    public String fileName() {
        return documentFileName;
    }

    public static List<DocumentType> resolve(String diagramTypes) {
        return Arrays.stream(diagramTypes.split(","))
                .map(DocumentType::valueOf)
                .collect(Collectors.toList());
    }
}

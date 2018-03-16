package jig.classlist.report.method;

import jig.classlist.report.Report;
import jig.classlist.report.ReportRow;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MethodReport implements Report {

    private final MethodPerspective perspective;
    private final List<MethodDetail> list;

    public MethodReport(MethodPerspective perspective, List<MethodDetail> list) {
        this.perspective = perspective;
        this.list = list;
    }

    @Override
    public ReportRow headerRow() {
        return perspective.headerLabel();
    }

    @Override
    public List<ReportRow> rows() {
        return list.stream().map(perspective::row).collect(toList());
    }
}
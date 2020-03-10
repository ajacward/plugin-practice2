package org.mystify.report;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;

public class ReportExtension {
  private final RegularFileProperty metadata;
  private final RegularFileProperty report;

  public ReportExtension(Project project) {
    metadata = project.getObjects().fileProperty();
    report = project.getObjects().fileProperty();

    metadata.convention(project.getLayout().getProjectDirectory().file("foss_metadata.json"));
    report.convention(
        project.getLayout().getBuildDirectory().file("reports/dep_report.csv"));
  }

  public RegularFileProperty getMetadata() {
    return metadata;
  }

  public RegularFileProperty getReport() {
    return report;
  }
}

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.mystify.report;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ReportPlugin implements Plugin<Project> {
  @Override
  public void apply(Project project) {
    ReportExtension extension =
        project.getExtensions().create("report", ReportExtension.class, project);

    project.getTasks().register("report", ReportTask.class, reportTask -> {
      reportTask.setGroup("Documentation");
      reportTask.setDescription("Generates dependency report");

      reportTask.getMetadata().set(extension.getMetadata());
      reportTask.getReport().set(extension.getReport());
    });
  }
}

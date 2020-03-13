package org.mystify.report;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

public class ReportTask extends DefaultTask {
  private Logger logger;

  private static final String DEFAUL_MVN_REPO_URL = "maven_url";
  private static final String DEFAUL_NPM_REPO_URL = "npm_url";

  private final RegularFileProperty metadata;
  private final RegularFileProperty report;

  private Map<String, Data> model;

  public ReportTask() {
    logger = getProject().getLogger();

    metadata = getProject().getObjects().fileProperty();
    report = getProject().getObjects().fileProperty();

    model = new HashMap<>();
  }

  @InputFile
  public RegularFileProperty getMetadata() {
    return metadata;
  }

  @OutputFile
  public RegularFileProperty getReport() {
    return report;
  }

  @TaskAction
  public void reportDependencies() {
    System.out.println(System.getProperty("catName"));
    System.out.println(System.getProperty("catAge"));

    populateModel();
    assignSourceUrl();
    generateReport();
  }

  private void populateModel() {
    logger.lifecycle("Collecting first order runtime classpath dependencies from {}",
        getProject().getName());

    if (List.of("java", "java-library", "application", "war", "ear").stream()
        .anyMatch(plugin -> getProject().getPluginManager().hasPlugin(plugin))) {

      getProject().getConfigurations().getByName("runtimeClasspath").getResolvedConfiguration()
          .getFirstLevelModuleDependencies()
          .forEach(dep -> {
            Data data = new Data(dep.getModuleGroup(), dep.getModuleName(), dep.getModuleVersion());

            model.putIfAbsent(data.keyName(), data);
          });
    }
  }

  private void assignSourceUrl() {
    logger.lifecycle("Assigning source URLs");

    // Filter out non foss from the model

    model.values().forEach(dep -> dep.setUrl(DEFAUL_MVN_REPO_URL));
  }

  private void generateReport() {
    logger.lifecycle("Writing report to {}", report.get());

    try (PrintWriter writer = new PrintWriter(new FileWriter(report.get().getAsFile()))) {
      writer.println("URL,VENDOR,NAME,VERSION,BRANCH");

      model.values().forEach(writer::println);
    } catch (IOException ioe) {
      logger.error(ioe.getMessage());
    }
  }
}

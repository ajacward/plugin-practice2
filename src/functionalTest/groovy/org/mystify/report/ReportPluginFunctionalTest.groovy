package org.mystify.report;

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.IgnoreRest
import spock.lang.Specification

/**
 * A simple functional test for the 'org.mystify.report' plugin.
 */
class ReportPluginFunctionalTest extends Specification {
  @Rule
  TemporaryFolder testProjectDir = new TemporaryFolder()
  File buildFile

  def setup() {
    buildFile = testProjectDir.newFile('build.gradle')
  }

  @IgnoreRest
  def "can call report task without extension declared"() {
    given:
    testProjectDir.newFile('foss_metadata.json')

    buildFile << """
            plugins {
                id 'org.mystify.report'
            }
    """

    when:
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withArguments('report')
        .withPluginClasspath()
        .forwardOutput()
        .build()

    then:
    result.task(':report').outcome == SUCCESS
  }

  def "can call report task with extension declared"() {
    given:
    File dst = testProjectDir.newFile('metadata.json')
    File src = new File('src/functionalTest/resources/foss.json')
    dst << src.getText()

    buildFile << """
            plugins {
                id 'org.springframework.boot' version '2.2.0.RELEASE'
                id 'io.spring.dependency-management' version '1.0.8.RELEASE'
                id 'java'
                id 'org.mystify.report'
            }

            repositories {
                mavenCentral()
            }

            dependencies {
              implementation 'org.springframework.boot:spring-boot-starter-web'
            }

            report {
                metadata = file('metadata.json')
                report = file('build/reports/dep_report.csv')
            }
    """

    when:
    def result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withArguments('report')
        .withPluginClasspath()
        .build()

    then:
    result.task(':report').outcome == SUCCESS
    def reportFile = new File(testProjectDir.root, 'build/reports/dep_report.csv')
    reportFile.exists()
    reportFile.getText().replace('\r', '') == new File('src/functionalTest/resources/expected.csv').getText()
  }
}

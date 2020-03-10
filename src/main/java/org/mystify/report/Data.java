package org.mystify.report;

public class Data {
  private String name;
  private String group;
  private String version;
  private String url;
  private String branch;

  public Data(String group, String name, String version) {
    this.group = group;
    this.name = name;
    this.version = version;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getBranch() {
    return branch;
  }

  public void setBranch(String branch) {
    this.branch = branch;
  }

  public String keyName() {
    return String.format("%s:%s:%s", group, name, version);
  }

  @Override
  public String toString() {
    return String.format("%s,%s,%s,%s,", url, group, name, version);
  }
}

#!/usr/bin/env groovy

def call() {
  def plugins = jenkins.model.Jenkins.instance.pluginManager.activePlugins
    .findAll { it.hasUpdate() }
    .collect { it.getShortName() }
  println "Plugins to upgrade: ${plugins.join(', ')}"
  long count = 0

  jenkins.model.Jenkins.instance.pluginManager.install(plugins, false).each {
    it.get()
    println "${++count}/${plugins.size()}.."
  }

  if(plugins.size() != 0 && count == plugins.size()) {
    jenkins.model.Jenkins.instance.safeRestart()
  }
}

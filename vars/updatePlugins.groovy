#!/usr/bin/env groovy

def call() {
  stage('Update Plugin Manager') {
    jenkins.model.Jenkins.getInstance().getUpdateCenter().getSites().each { site ->
      site.updateDirectlyNow(hudson.model.DownloadService.signatureCheck)
    }

    hudson.model.DownloadService.Downloadable.all().each { downloadable ->
      downloadable.updateNow();
    }
  }

  stage('List plugins to update') {
    def plugins = jenkins.model.Jenkins.instance.pluginManager.activePlugins
      .findAll { it -> it.hasUpdate() }
      .collect { pl -> pl.getShortName() }
    println "Plugins to upgrade: ${plugins.join(', ')}"
  }

  long count = 0

  stage('Update plugins') {
    jenkins.model.Jenkins.instance.pluginManager.install(plugins, false).each { f ->
      f.get()
      println "${++count}/${plugins.size()}.."
    }
  }

  if(plugins.size() != 0 && count == plugins.size()) {
    jenkins.model.Jenkins.instance.safeRestart()
  }
}

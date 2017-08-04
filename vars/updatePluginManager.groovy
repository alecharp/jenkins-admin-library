#!/usr/bin/env groovy

def call() {
  jenkins.model.Jenkins.getInstance().getUpdateCenter().getSites().each { site ->
    site.updateDirectlyNow(hudson.model.DownloadService.signatureCheck)
  }

  hudson.model.DownloadService.Downloadable.all().each { downloadable ->
    downloadable.updateNow();
  }
}

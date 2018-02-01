package com.thoughtworks.sbtApiMappings

import sbt._
import com.thoughtworks.Extractor._

object SonatypeApiMappingRule extends AutoPlugin {

  import ApiMappings.autoImport._

  override def requires = ApiMappings

  override def trigger = allRequirements

  private def moduleID: ModuleID => (String, String, String) = { moduleID =>
    (moduleID.organization, moduleID.name, moduleID.revision)
  }

  private def sonatypeRule: PartialFunction[ModuleID, URL] = {
    case moduleID.extract(organization, libraryName, revision) =>
      val organizationPath = organization.replace('.', '/')
      url(s"https://oss.sonatype.org/service/local/repositories/public/archive/$organizationPath/$libraryName/$revision/$libraryName-$revision-javadoc.jar/!/index.html")
  }

  override def projectSettings = {
    apiMappingRules := sonatypeRule.orElse(apiMappingRules.value)
  }

}

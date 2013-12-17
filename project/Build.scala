import sbt._

import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "cos2"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    filters,
    "com.clarkparsia" % "imperium2_2.10" % "0.1-SNAPSHOT"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add custom repository:
    resolvers += Resolver.file("Local ivy repo", file("../../.ivy2/local"))(Resolver.ivyStylePatterns),

    closureCompilerOptions += "ecmascript5",

    lessEntryPoints <<= baseDirectory(_ / "app" / "assets" / "less" ** "styles.less")
  )

}

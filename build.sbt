val ZioVersion        = "1.0.0-RC17"
val ZioCatsVersion    = "2.0.0.0-RC8"
val ContVersion       = "0.2.0-M2"
val CatsEffVersion    = "2.0.0"
val FlywayVersion     = "6.0.8"
val TcVersion         = "0.33.0"
val PSQLDriverVersion = "42.2.8"
val ScalatestVersion  = "3.0.8"
val PsqlContVersion   = "1.12.3"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

lazy val root = (project in file("."))
  .settings(
    organization := "Neurodyne",
    name := "zio-containers",
    version := "0.0.1",
    scalaVersion := "2.12.10",
    maxErrors := 3,
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % ZioVersion,
      // "dev.zio" %% "zio-interop-cats" % ZioCatsVersion,
      // "org.typelevel"     %% "cats-effect"           % CatsEffVersion,
      //"org.flywaydb" % "flyway-core" % FlywayVersion,
      // "dev.zio"       %% "zio-test"     % ZioVersion % "test",
      // "dev.zio"       %% "zio-test-sbt" % ZioVersion % "test",
      "org.scalatest"      %% "scalatest"            % ScalatestVersion % "test",
      "com.dimafeng"       %% "testcontainers-scala" % TcVersion % "test",
      "org.testcontainers" % "postgresql"            % PsqlContVersion,
      "org.postgresql"     % "postgresql"            % PSQLDriverVersion
      // "io.chrisdavenport" %% "testcontainers-specs2" % ContVersion % "test"
    )
  )
// testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))

// Refine scalac params from tpolecat
scalacOptions --= Seq(
  "-Xfatal-warnings"
)
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

// Aliases
addCommandAlias("rel", "reload")
addCommandAlias("com", "all compile test:compile it:compile")
addCommandAlias("lint", "; compile:scalafix --check ; test:scalafix --check")
addCommandAlias("fix", "all compile:scalafix test:scalafix")
addCommandAlias("fmt", "all scalafmtSbt scalafmtAll")
addCommandAlias("chk", "all scalafmtSbtCheck scalafmtCheckAll")
addCommandAlias("cov", "; clean; coverage; test; coverageReport")

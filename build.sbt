lazy val commonSettings = Seq(
    scalaVersion := "2.12.3",
    resolvers ++= Seq(
        Resolver.sonatypeRepo("releases"),
        Resolver.sonatypeRepo("snapshots")),
    scalacOptions ++= Seq(
        "-feature",
        "-deprecation",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-Ypartial-unification"),
    libraryDependencies ++= Seq(
        compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4"))
)

lazy val catsSettings = Seq(
    libraryDependencies ++= Seq(
        "org.typelevel" %% "cats-core" % "0.9.0",
        "org.typelevel" %% "cats-free" % "0.9.0",
        "org.scalatest" %% "scalatest" % "3.0.4" % "test")
)

lazy val trek = project.in(file("."))
    .settings(moduleName := "free-trek")
    .settings(commonSettings)
    .aggregate(trekDomain, trekCats)
    .dependsOn(trekDomain, trekCats)

lazy val trekDomain = project.in(file("trek-domain"))
    .settings(moduleName := "trek-domain")
    .settings(commonSettings)

lazy val trekCats = project.in(file("trek-cats"))
    .settings(moduleName := "trek-cats")
    .settings(commonSettings)
    .settings(catsSettings)
    .dependsOn(trekDomain)

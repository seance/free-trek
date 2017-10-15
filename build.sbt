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

lazy val freesSettings = Seq(
    libraryDependencies ++= Seq(
        compilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full),
        "io.frees" %% "frees-core" % "0.4.0",
        "org.typelevel" %% "cats-core" % "1.0.0-MF",
        "org.typelevel" %% "cats-mtl-core" % "0.0.2",
        "org.scalatest" %% "scalatest" % "3.0.4" % "test")
)

lazy val trek = project.in(file("."))
    .settings(moduleName := "free-trek")
    .settings(commonSettings)
    .aggregate(trekDomain, trekCats, trekFrees)
    .dependsOn(trekDomain, trekCats, trekFrees)

lazy val trekDomain = project.in(file("trek-domain"))
    .settings(moduleName := "trek-domain")
    .settings(commonSettings)

lazy val trekCats = project.in(file("trek-cats"))
    .settings(moduleName := "trek-cats")
    .settings(commonSettings)
    .settings(catsSettings)
    .dependsOn(trekDomain)

lazy val trekFrees = project.in(file("trek-frees"))
    .settings(moduleName := "trek-frees")
    .settings(commonSettings)
    .settings(freesSettings)
    .dependsOn(trekDomain)

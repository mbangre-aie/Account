organization in ThisBuild := "com.retisio.arc"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.13.0"
lagomKafkaEnabled in ThisBuild := false
lagomCassandraEnabled in ThisBuild := false
lazy val `account` = (project in file("."))
  .aggregate(`account-api`, `account-impl`)

lazy val `account-api` = (project in file("account-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lombok
    )
  )

lazy val `account-impl` = (project in file("account-impl"))
  .enablePlugins(LagomJava, JavaAgent)
  .settings(common,Dependencies.kamonSettings)
  .settings(
    libraryDependencies ++= Seq(
      lombok
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`account-api`)

sources in(Compile, doc) := Seq.empty

val lombok = "org.projectlombok" % "lombok" % "1.18.8"

def common = Seq(
  dockerExposedPorts := Seq(9000, 8558, 9091, 10001),
  dockerBaseImage := "openjdk:8-jre-slim",
  sources in(Compile, doc) := Seq.empty,
  javacOptions in Compile := Seq("-g", "-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation", "-parameters"),
  libraryDependencies ++= Dependencies.dependencies
)

lagomServiceLocatorAddress in ThisBuild := "0.0.0.0"
lagomServiceGatewayAddress in ThisBuild := "0.0.0.0"

import com.lightbend.lagom.sbt.LagomImport._
import com.lightbend.sbt.javaagent.JavaAgent.JavaAgentKeys.javaAgents
import sbt.Keys.libraryDependencies
import sbt._

object Version {
  val akkaMgmtVersion = "1.1.0"
  val akkaVersion = "2.6.14"
  val alpakkaVersion = "2.1.0"
  val postgresVersion = "42.2.10"
}

object Dependencies {
  val dependencies = Seq(
    lagomJavadslPersistenceJdbc,
    lagomJavadslKafkaBroker,
    lagomLogback,
    lagomJavadslTestKit,
    lagomJavadslAkkaDiscovery,
    lagomJavadslCluster,
    lagomJavadslApi,
    "com.typesafe.akka" %% "akka-persistence" % Version.akkaVersion,
    "com.lightbend.akka.discovery" %% "akka-discovery-kubernetes-api" % Version.akkaMgmtVersion,
    "com.lightbend.akka.management" %% "akka-management-cluster-http" % Version.akkaMgmtVersion,
    "com.typesafe.akka" %% "akka-cluster" % Version.akkaVersion,
    "com.typesafe.akka" %% "akka-stream-kafka" % Version.alpakkaVersion,
    "org.postgresql" % "postgresql" % Version.postgresVersion
  )

  val kamonSettings = Seq(
    javaAgents += "io.kamon" % "kanela-agent" % "1.0.13",
    /*javaOptions in Universal += "-DKamon.auto-start=true",*/
    libraryDependencies ++= Seq(
      "io.kamon" %% "kamon-bundle" % "2.4.2",
      "io.kamon" %% "kamon-prometheus" % "2.4.2"
    ))

}

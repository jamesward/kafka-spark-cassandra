import java.io.File
import java.net.URL

import org.apache.log4j.{Level, LogManager, Logger, PropertyConfigurator}
import play.api.{Environment, LoggerConfigurator, Mode}

class Log4J2LoggerConfigurator extends LoggerConfigurator {

  override def init(rootPath: File, mode: Mode.Mode): Unit = {
    val properties = Map("application.home" -> rootPath.getAbsolutePath)
    val resourceName = "log4j.properties"
    val resourceUrl = Option(this.getClass.getClassLoader.getResource(resourceName))
    configure(properties, resourceUrl)
  }

  override def shutdown(): Unit = {
    LogManager.shutdown()
  }

  override def configure(env: Environment): Unit = {
    val properties = Map("application.home" -> env.rootPath.getAbsolutePath)
    val resourceUrl = env.resource("log4j.properties")
    configure(properties, resourceUrl)
  }

  override def configure(properties: Map[String, String], config: Option[URL]): Unit = {
    PropertyConfigurator.configure(config.get)
  }

}

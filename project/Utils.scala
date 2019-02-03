import scala.sys.process._
import scala.util.Try

object Utils {

  private def run(command: String): Option[String] = Try(command.!!).toOption.map(_.trim)

  def gitBranchName(): Option[String] = run("git rev-parse --abbrev-ref HEAD")
  
  def gitCommitHash(): Option[String] = run("git rev-parse HEAD")

}

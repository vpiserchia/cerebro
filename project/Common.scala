import java.io.File
import scala.util.matching.Regex

object Common {
  def remapPath(oldPath: String, newPath: String, prefixes: String*): ((File, String)) => (File, String) = {
    case (file, path) =>
      file -> prefixes
        .find(p => path.startsWith(s"$p/$oldPath"))
        .fold(path)(p => s"$p/$newPath" + path.drop(p.length + 1 + oldPath.length))
  }
}

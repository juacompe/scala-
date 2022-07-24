import scala.io.Source
import scala.xml.{XML, Node, NodeSeq}

object EntryPoint extends App {
  val urls = for {
    line <- Source.fromFile("source.txt").getLines.toArray
    if line.contains("http://")
  } yield line

  val keyword = "Ukraine"
  val content = fetchAllResults(urls)
  writeFile(getFileName(keyword), content)

  def fetchAllResults(urls: Array[String]): String = {
    if (urls.isEmpty) ""
    else
      fetchFilteredResultsAsString(urls.head).concat(fetchAllResults(urls.tail))
  }

  def fetchFilteredResultsAsString(url: String) = {
    def searchByKeyword = search(keyword) _
    (fetchResults _ andThen searchByKeyword)(url).mkString("\n")
  }

  def fetchResults(url: String): Seq[String] = {
    val responseBody = getResponseBody(url)
    val titles = XML.loadString(responseBody) \\ "item" \ "title"
    for { (title) <- titles } yield title.text
  }

  def getResponseBody(url: String): String = {
    import scalaj.http._
    val response: HttpResponse[String] = Http(url).asString
    return response.body
  }

  def search(keyword: String)(titles: Seq[String]): Seq[String] = {
    for {
      title <- titles
      if (title.contains(keyword))
    } yield title
  }

  def writeFile(filename: String, content: String) = {
    import java.io.PrintWriter
    new PrintWriter(filename) { write(content); close }
  }

  def getFileName(keyword: String): String = {
    import java.text.SimpleDateFormat
    import java.util.Calendar
    val format = new SimpleDateFormat("d-M-y-hh-mm")
    val fileName = format.format(Calendar.getInstance().getTime())
    keyword + "-" + fileName
  }
}

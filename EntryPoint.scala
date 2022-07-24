object EntryPoint extends App {
  val keyword = "Ukraine"
  val content =
    (readSourcesFromFile _ andThen
      fetchAllResults(filter(keyword)) _)("source.txt")

  writeFile(getFileName(keyword))(content)

  def readSourcesFromFile(fileName: String) = {
    import scala.io.Source
    for {
      line <- Source.fromFile(fileName).getLines.toArray
      if line.contains("http://")
    } yield line
  }

  def fetchAllResults(filter: (Seq[String]) => Seq[String])(
      urls: Array[String]
  ): String = {
    @annotation.tailrec
    def loop(urls: Array[String], sum: String): String = {
      if (urls.isEmpty) sum
      else {
        def fetchFilteredResultsAsString(url: String) =
          (fetchResults _ andThen filter)(url).mkString("\n")
        loop(urls.tail, fetchFilteredResultsAsString(urls.head).concat(sum))
      }
    }
    loop(urls, "")
  }

  def fetchResults(url: String): Seq[String] = {
    import scala.xml.{XML, Node, NodeSeq}
    val responseBody = getResponseBody(url)
    val titles = XML.loadString(responseBody) \\ "item" \ "title"
    for { (title) <- titles } yield title.text
  }

  def getResponseBody(url: String): String = {
    import scalaj.http._
    val response: HttpResponse[String] = Http(url).asString
    return response.body
  }

  def filter(keyword: String)(titles: Seq[String]): Seq[String] = {
    for {
      title <- titles
      if (title.contains(keyword))
    } yield title
  }

  def writeFile(filename: String)(content: String) = {
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

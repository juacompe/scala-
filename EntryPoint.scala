import scala.io.Source
import scala.xml.{XML, Node, NodeSeq}

object EntryPoint extends App {
  val urls = for {
    line <- Source.fromFile("source.txt").getLines
    if line.contains("http://")
  } yield line

  val keyword = "Ukraine"
  var titles = Seq[String]()
  urls.foreach(url => {
    val results = getResults(url, keyword)
    titles = titles ++ results
  })
  var content = ""
  for (title <- titles) content = content.concat(title + "\n")
  writeFile(getFileName(keyword), content)

  def getResults(url: String, keyword: String): Seq[String] = {
    val responseBody = getResponseBody(url)
    val titles = XML.loadString(responseBody) \\ "item" \ "title"
    val results = search(keyword, titles)
    return results
  }

  def getResponseBody(url: String): String = {
    import scalaj.http._
    val response: HttpResponse[String] = Http(url).asString
    return response.body
  }

  def search(keyword: String, titles: NodeSeq): Seq[String] = {
    val results = for {
      title <- titles
      if (title.text.contains(keyword))
    } yield title.text
    return results
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

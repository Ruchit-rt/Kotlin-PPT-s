package websearch

import org.jsoup.Jsoup.connect
import org.jsoup.Jsoup.parse
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

data class URL(private val url: String) {
  override fun toString(): String = url
  override fun equals(other: Any?): Boolean {
    if (other is URL) return this.url == other.url
    return false
  }
  fun download(): WebPage = WebPage(connect(url).get())
}

class WebPage(val document: Document) {
  override fun toString(): String = TODO()

  fun extractWords(): List<String> {
    val text = document.text()
    return text.split(" ", ", ", ".").filter { t -> t != "" }.map { t -> t.lowercase() }
  }

  fun extractLinks(): List<URL> {
    val aTags: Elements = document.getElementsByTag("a")
    val links = mutableListOf<URL>()
    for (tag in aTags) {
      val link = tag.attr("href")
      if (link.startsWith("http://") || link.startsWith("https://"))  links.add(URL(link))
    }
    return links
  }
}

// testing in main - extract links
fun main() {
  val htmlString =
    """
            <html>
              <head>
                <title>Simple Page</title>
              </head>
              <body>
                <p>This is a simple <a href="https://en.wikipedia.org/wiki/HTML">HTML</a> document.</p>
                <p>But it has two <a href="https://www.w3schools.com/html/html_links.asp">links</a>.</p>
              </body>
            </html>"""
  val htmlDocument: Document = parse(htmlString)
  val page = WebPage(htmlDocument)
  println(page.extractLinks())
  print(
    listOf(
      URL("https://en.wikipedia.org/wiki/HTML"),
      URL("https://www.w3schools.com/html/html_links.asp")
    )
  )
}

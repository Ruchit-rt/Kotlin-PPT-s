package websearch

import java.lang.Exception

class WebCrawler(val startFrom: URL) {
  private val maxPages = 10
  private val m = mutableMapOf<URL, WebPage>()
  private val notM = mutableListOf<URL>()
  fun run(startFrom: URL = this.startFrom) {
    var page1: WebPage? = null
    try {
      page1 = startFrom.download()
    } catch (e: Exception) {
      notM.add(startFrom)
      return
    }
    m[startFrom] = page1
    val links = page1.extractLinks().distinct().drop(1)
    if (links.isNotEmpty()) {
      for (link in links) if (!m.keys.contains(link) && (!notM.contains(link) && m.size <= maxPages)) run(link)
    }
  }

  fun dump(): Map<URL, WebPage> {
    return m
  }
}

fun main() {
  val crawler = WebCrawler(startFrom = URL("http://www.bbc.co.uk"))
  crawler.run()
  val searchEngine = SearchEngine(crawler.dump())
  searchEngine.compileIndex()
  println(searchEngine.searchFor("news"))
}

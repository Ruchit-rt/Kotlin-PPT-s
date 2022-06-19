package websearch

class SearchEngine(val corpus: Map<URL, WebPage>) {
  val index: MutableMap<String, List<SearchResult>> = mutableMapOf()
  fun compileIndex() {
    val pairs = mutableListOf<Pair<String, URL>>()
    for (p in corpus) {
      val words = p.value.extractWords()
      for (word in words) {
        pairs.add(Pair(word, p.key))
      }
    }
    val grouped = pairs.groupBy { p -> p.first }
    for (w in grouped) {
      val ls = w.value.map { l -> l.second }
      index[w.key] = rank(ls)
    }
  }

  fun rank(ls: List<URL>): List<SearchResult> {
    return ls.groupBy { it }.map { g -> SearchResult(g.key, g.value.size) }.sortedByDescending { it.numRefs }
  }

  fun searchFor(query: String): SearchResultsSummary =
    if (index.containsKey(query)) {
      SearchResultsSummary(query, index.getOrDefault(query, listOf()))
    } else SearchResultsSummary(query, listOf())
}

class SearchResult(val url: URL, val numRefs: Int)

class SearchResultsSummary(val query: String, val results: List<SearchResult>) {
  override fun toString(): String {
    val s = StringBuilder()
    s.append("Results for $query:\n")
    for (r in results) {
      s.append("\t ${r.url} - ${r.numRefs} references\n")
    }
    return s.toString()
  }
}

// testing various queries through main function
fun main() {
  val searchEngine = SearchEngine(downloadedWebPages)
  searchEngine.compileIndex()
  val summary = searchEngine.searchFor("software")
  println(summary)
}
// fun main(){
//  val docHomePageHtml =
//    """<html>
//         <head>
//           <title>Department of Computing</title>
//         </head>
//           <body>
//             <p>Welcome to the Department of Computing at <a href="https://www.imperial.ac.uk">Imperial College London</a>.</p>
//           </body>
//        </html>"""
//  val imperialHomePageHtml =
//    """<html>
//         <head>
//           <title>Imperial College London</title>
//         </head>
//           <body>
//             <p>Imperial people share ideas, expertise and technology to find answers to the big scientific questions and tackle global challenges</p>
//             <p>See the latest news about our research on the <a href="https://www.bbc.co.uk/news">BBC</a></p>
//           </body>
//        </html>"""
//  val bbcNewsPageHtml =
//    """<html>
//         <head>
//           <title>BBC News</title>
//         </head>
//           <body>
//             <p>Here is all the latest news about science and other things.</p>
//           </body>
//        </html>"""
//
//  val docHomePage = WebPage(Jsoup.parse(docHomePageHtml))
//  val imperialHomePage = WebPage(Jsoup.parse(imperialHomePageHtml))
//  val bbcNews = WebPage(Jsoup.parse(bbcNewsPageHtml))
//  val downloadedPages = mapOf(
//    URL("https://www.imperial.ac.uk/computing") to docHomePage,
//    URL("https://www.imperial.ac.uk") to imperialHomePage,
//    URL("https://www.bbc.co.uk/news") to bbcNews )
//  val searchEngine = SearchEngine(downloadedPages)
//  searchEngine.compileIndex()
//  val summary = searchEngine.searchFor("news")
//  print(summary)
// }

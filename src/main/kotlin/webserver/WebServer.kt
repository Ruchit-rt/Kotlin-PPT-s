package webserver

// write your web framework code here:

fun scheme(url: String): String = url.substringBefore("://")

fun host(url: String): String = url.substringAfter("://").substringBefore("/")

fun path(url: String): String = url.substringAfter(host(url)).substringBefore("?")

fun queryParams(url: String): List<Pair<String, String>> {
  return if (url.contains('?')) {
    val queries = url.substringAfter("?").split("&")
    queries.map { q ->
      val values = q.split('=')
      Pair(values[0], values[1])
    }
  } else {
    listOf()
  }
}

// http handlers for a particular website...

fun homePageHandler(request: Request): Response = Response(Status.OK, "This is Imperial.")

fun helloHandler(r: Request): Response {
  val queries = queryParams(r.url)
  val indexes = queries.map { q -> q.first }
  val body = if (queries.isEmpty()) {
    "Hello, World!"
  } else {
    if (indexes.contains("style")) {
      if (indexes.contains("name")) {
        val i = indexes.indexOf("name")
        "HELLO, " + queries[i].second.uppercase() + "!"
      } else {
        "HELLO, WORLD!"
      }
    } else {
      val i = indexes.indexOf("name")
      "Hello, " + queries[i].second + "!"
    }
  }
  val stat = Status.OK
  return Response(stat, body)
}

fun computingHandler(r: Request): Response = Response(Status.OK, "This is DoC.")
fun examHandler(r: Request): Response = Response(Status.OK, "This is very secret.")
fun nothingHandler(r: Request): Response = Response(Status.NOT_FOUND, "404")

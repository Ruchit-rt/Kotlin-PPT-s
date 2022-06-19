package webserver

// provided files

class Request(val url: String, val authToken: String = "")

class Response(val status: Status, val body: String = "")

enum class Status(val code: Int) {
  OK(200),
  FORBIDDEN(403),
  NOT_FOUND(404)
}
typealias HttpHandler = (Request) -> Response

val handlers = mapOf(
  "/say-hello" to ::helloHandler,
  "/" to ::homePageHandler,
  "/computing" to ::computingHandler,
  "/exam-marks" to requireToken(::examHandler, "password1")
)

fun configureRoutes(mapping: Map<String, HttpHandler>): HttpHandler {
  return fun(r: Request): Response {
    val handleName = path(r.url)
    val func = mapping.getOrDefault(handleName, ::nothingHandler)
    return func(r)
  }
}

fun requireToken(wrapped: HttpHandler, token: String = "No token provided"): HttpHandler {
  return fun(r: Request): Response {
    return if (r.authToken == token) wrapped(r)
    else Response(Status.FORBIDDEN, "403")
  }
}

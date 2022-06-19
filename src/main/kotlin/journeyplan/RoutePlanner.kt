package journeyplan

typealias Sorter = (Route) -> Int

class SubwayMap(val segments: List<Segment>) {
  override fun toString(): String {
    TODO()
  }

  fun routesFrom(origin: Station, destination: Station, optimisingFor: Sorter = Route::duration): List<Route> {
    fun routesFromhelper(origin: Station, destination: Station, travelhistory: List<Station>): List<Route> {
      val routes = mutableListOf<Route>()
      if (origin != destination) {
        val originSegments = segments.filter { s -> s.start == origin }.filterNot { s -> travelhistory.contains(s.end) }
        for (o in originSegments) {
          val newOrigin = o.end
          if (newOrigin != destination) {
            val router = routesFromhelper(newOrigin, destination, listOf(newOrigin) + travelhistory)
            val finalRoutes = router.map { r -> Route(listOf(o) + (r.segments)) }
              .filter { r -> r.segments.all { s -> s.line.status == Status.RUNNING } }
            routes.addAll(finalRoutes)
          } else {
            if (o.line.status == Status.RUNNING) routes.add(Route(listOf(o)))
          }
        }
      }
      return routes.toList()
    }

    val rs = (routesFromhelper(origin, destination, listOf(origin))).sortedBy { r -> optimisingFor(r) }
    val finalrs = mutableListOf<Route>()
    for (r in rs) {
      val segs = r.segments
      val n = segs.size - 1
      var counter = 0
      for (i in 0..n) {
        if (segs[i].end.status == Status.CLOSED) {
          if (i == n) {
            counter += 1
          } else if (segs[i].line != segs[i + 1].line) {
            counter += 1
          }
        }
      }
      if (counter == 0) {
        finalrs.add(r)
      }
    }
    return finalrs.toList()
  }
}

class Route(val segments: List<Segment>) {

  override fun toString(): String {
    val printer = StringBuilder()
    var tracker = segments[0].line
    printer.append(segments[0].start.toString() + " to " + segments.last().end.toString())
    printer.append(" - " + duration() + " minutes,")
    printer.append(" " + numChanges() + " changes")
    val n = segments.size - 1
    printer.append("\n - " + segments[0].start.toString() + " to ")
    for (i in 1..n) {
      if ((i != n) && (segments[i + 1].line == tracker)) {
        continue
      } else {
        printer.append(segments[i].end.toString() + " by " + tracker.toString())
        if (i != n) {
          printer.append("\n - " + segments[i + 1].start.toString() + " to ")
          tracker = segments[i + 1].line
        }
      }
    }
    return printer.toString()
  }

  fun duration(): Int {
    var sum = 0
    for (i in segments) {
      sum += i.time
    }
    return sum
  }

  fun numChanges(): Int {
    var changes = 0
    val n = segments.size - 1
    for (i in 0..n) {
      if (i != n) {
        if (segments[i].line == segments[i + 1].line) {
          continue
        } else changes += 1
      } else continue
    }
    return changes
  }
}

fun main() {
  val map = londonUnderground()
  for (i in map.routesFrom(southKensington, oxfordCircus)) {
    println(i)
  }
}

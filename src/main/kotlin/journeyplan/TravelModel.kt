package journeyplan

class Station(val name: String, var status: Status = Status.OPEN) {
  override fun toString(): String = name
  fun close() {
    this.status = Status.CLOSED
  }

  fun open() {
    this.status = Status.OPEN
  }
}

class Line(val name: String, var status: Status = Status.RUNNING) {
  override fun toString(): String = "$name Line"
  fun suspend() {
    this.status = Status.SUSPENDED
  }

  fun resume() {
    this.status = Status.RUNNING
  }
}

enum class Status {
  RUNNING,
  SUSPENDED,
  OPEN,
  CLOSED
}

class Segment(val start: Station, val end: Station, val line: Line, val time: Int) {
  override fun toString(): String = "Start: $start  End: $end  Line: $line  Traveltime: $time"
}

val northernLine = Line("Northern")
val victoriaLine = Line("Victoria")
val centralLine = Line("Central")
val piccadillyLine = Line("Piccadilly")
val districtLine = Line("District")

val southkensington = Station("South Kensington")
val highgate = Station("Highgate")
val archway = Station("Archway")
val tufnellPark = Station("Tufnell Park")
val kentishTown = Station("Kentish Town")
val camden = Station("Camden Town")
val euston = Station("Euston")
val warrenStreet = Station("Warren Street")
val bondStreet = Station("Bond Street")
val southKensington = Station("South Kensington")
val knightsbridge = Station("Knightsbridge")
val hydeParkCorner = Station("Hyde Park Corner")
val greenPark = Station("Green Park")
val oxfordCircus = Station("Oxford Circus")
val victoria = Station("Victoria")
val sloaneSquare = Station("Sloane Square")

fun londonUnderground(): SubwayMap = SubwayMap(
  listOf(
    Segment(southKensington, knightsbridge, piccadillyLine, 3),
    Segment(knightsbridge, hydeParkCorner, piccadillyLine, 4),
    Segment(hydeParkCorner, greenPark, piccadillyLine, 2),
    Segment(greenPark, oxfordCircus, victoriaLine, 1),
    Segment(greenPark, victoria, victoriaLine, 1),
    Segment(victoria, greenPark, victoriaLine, 1),
    Segment(victoria, sloaneSquare, districtLine, 6),
    Segment(sloaneSquare, southKensington, districtLine, 3),
    Segment(southKensington, sloaneSquare, districtLine, 6),
    Segment(sloaneSquare, victoria, districtLine, 6)
  )
)

package plot

import viz.PlotTarget
import viz.PlotTargets.desktopBrowser // tells us what to do with the spec
import viz.vega.plots._ // import all the plots...
import ujson.Value
import viz.FromResource

object simpler {
  def main(args: Array[String]): Unit = {
    val data: Seq[(String, String, Seq[(Double, Double)])] =
      Seq(
        ("series 1", "black", Seq((5, 6), (16, 46))),
        ("series 2", "red", Seq((3, 1), (15, 9)))
      )

    val data1: Seq[(String, String, Seq[(Double, Double)])] = (
      Seq(
        ("series 1", "steelblue", Seq((1, 2), (3, 4))),
        ("series 2", "green", Seq((3, 6), (8, 9)))
      )
    )

    case class SeriesScatter2(override val mods: Seq[ujson.Value => Unit] = List()) extends FromResource {
      override lazy val path = "SeriesScatter2.json"
    }

    def seriesScatterPlot(title: String, data: Seq[(String, String, Seq[(Double, Double)])]) = {

      val polishData = data.map {
        case (seriesName, colour, aSeq) => {

          aSeq.map { point =>
            ujson.Obj(
              "series" -> seriesName,
              "colour" -> colour,
              "x" -> point._1,
              "y" -> point._2
            )
          }
        }
      }.flatten      

      SeriesScatter2(
        List(
          (spec: ujson.Value) => spec("title") = title, // set title
          (spec: Value) => spec("data") = ujson.Arr(ujson.Obj("name" -> "source", "values" -> polishData)),
          (spec: Value) => spec("scales")(1)("zero") = false,
          (spec: Value) => spec("scales")(2)("range") = ujson.Obj("data" -> "source", "field"->"colour")
        )
      )
    }

    val chart1 = seriesScatterPlot("chart 1", data)
    val chart2 = seriesScatterPlot("chart 2", data1)

  }
}

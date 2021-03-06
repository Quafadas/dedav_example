package plot

import viz.PlotTargets.desktopBrowser // tells us what to do with the spec
import viz.vega.plots._ // import all the plots...
import ujson.Value

object main {
  def main(args: Array[String]): Unit = {

    val data: (String, Seq[(String, Seq[(Double, Double)])]) = (
      "Fab Chart",
      List(
        ("black", List((1, 2), (3, 4))),
        ("red", List((3, 6), (8, 9)))
      )
    )

    val polishData = data._2.map {
      case (colour, aSeq) => {
        aSeq.map { point =>
          ujson.Obj(
            "colour" -> colour,
            "x" -> point._1,
            "y" -> point._2
          )
        }
      }
    }.flatten

    val s = ScatterPlot(
      Seq(
        (spec: ujson.Value) => spec("title") = data._1, // set title
        (spec: Value) => spec("data") = ujson.Arr(ujson.Obj("name" -> "source", "values" -> polishData)),
        (spec: Value) => spec("axes")(0)("title") = "x",
        (spec: Value) => spec("axes")(1)("title") = "y",
        (spec: Value) => spec("marks")(0)("encode")("update")("x")("field") = "x",
        (spec: Value) => spec("scales")(0)("domain")("field") = "x",
        (spec: Value) => spec("scales")(1)("domain")("field") = "y",
        (spec: Value) => { spec("scales").arr.dropRightInPlace(1); () },
        (spec: Value) => spec("marks")(0)("encode")("update")("y")("field") = "y",
        (spec: Value) => spec("marks")(0)("encode")("update")("stroke") = ujson.Obj("field" -> "colour"),
        (spec: Value) => spec("marks")(0)("encode")("update")("size") = ujson.Obj("value" -> 12),
        (spec: Value) => spec("marks")(0)("encode")("update")("opacity") = ujson.Obj("value" -> 1),
        (spec: Value) => { spec.obj.remove("legends"); () },
        viz.Utils.fillDiv
      )
    )

    println(s.spec)

  }
}

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

      SeriesScatter(
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

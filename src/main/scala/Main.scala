package splitter


object Main extends App {

  import pdf._

  args match {
    case Array(target, base) => doMain(target, base)
    case _ => {
      println("Invalid arguments.")
      System.exit(1)
    }
   }


  def doMain(target: String, base: String): Unit = {

    PDF.loan(PDF.open(target)) { pdf =>

      val pageRanges = pdf.toc
        .map({ bookmark => (bookmark.title, bookmark.pageNumber) })
        .sliding(2)
        .map({ case Stream((title, start), (_, nextStart)) => (title, start, nextStart - 1) })
        .filter({ case (title, _, _) => title.startsWith("file:") })
        .map({ case (title, start, stop) => (title.drop(5), start, stop) })

      for ((title, start, stop) <- pageRanges) {
        val section = PDF.create(pdf.pages.slice(start, stop))
        section.save(s"$base/$title.pdf")
        section.close
      }

    }
  }

}

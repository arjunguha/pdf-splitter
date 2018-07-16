package pdf

class PDF private (doc: org.apache.pdfbox.pdmodel.PDDocument) extends AutoCloseable {
  import java.util.ArrayList
  import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem
  import org.apache.pdfbox.pdmodel.{PDPage, PDDocument}

  private val catalog = doc.getDocumentCatalog

  val pages = {
    import scala.collection.JavaConversions._
    val lst = new ArrayList[PDPage]()
    catalog.getPages.getAllKids(lst) // crazy API
    lst.toSeq
  }

  val toc = {
    val outline = catalog.getDocumentOutline
    if (outline == null) {
      Stream[Bookmark]()
    }
    else {
      siblings(outline.getFirstChild).map(outline => new Bookmark(outline))
    }
  }

  private def siblings(item: PDOutlineItem): Stream[PDOutlineItem] = {
    if (item == null) {
      Stream()
    }
    else {
      item #:: siblings(item.getNextSibling)
    }
  }

  class Bookmark private[PDF](outline: PDOutlineItem) {
    lazy val title = outline.getTitle

    val children: Stream[Bookmark] = siblings(outline.getFirstChild).map(outline => new Bookmark(outline))

    lazy val pageNumber = {
      val n = pages.indexOf(outline.findDestinationPage(doc))
      assert(n >= 0, "bookmark points to an unknown page")
      n
    }

    override def toString(): String = s"Bookmark($title)"
  }

  def close() = doc.close()

  def save(path: String) = doc.save(path)
}

object PDF {

  import org.apache.pdfbox.pdmodel.{PDPage, PDDocument}

  def loan[A <: AutoCloseable,B](resource: A)(body: A => B): B = {
    try {
      body(resource)
    }
    finally {
      resource.close()
    }
  }

  def create(pages: Seq[PDPage]): PDF = {
    val doc = new PDDocument()
    for (page <- pages) {
      doc.addPage(page)
    }
    new PDF(doc)
  }

  def open(path: String): PDF = {
    new PDF(PDDocument.load(path))
  }
}

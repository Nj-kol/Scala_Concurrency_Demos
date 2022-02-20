package example.future

import java.sql.SQLException
import scala.concurrent.Future
import scala.util.Success

object ComposeFutures {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def composefuture1(): Future[Int] = {
    val future1 = Future { getData1() }
    val combined = future1.map(n1 => n1 + getData2())
    combined
  }

  def composefuture2(): Future[Int] = {
    val future1 = Future { getData1() }
    val future2 = Future { getData2() }
    val combined= future1.flatMap(n1 => future2.map(n2 => n1 + n2))
    combined
  }

  def composefuture3(): Future[Int] = {
    val future1 = Future { getData1() }
    val future2 = Future { getData2() }
    val combined = for (n1 <- future1; n2 <- future2 if n1 != n2) yield n1 + n2

    future1.zipWith(future2)(_ + _)
    combined
  }

  def demoForEach(): Unit = {
    val future1 = Future { getData1() }
    val future2 = Future { getData2() }
    val combined = for (n1 <- future1; n2 <- future2) yield n1 + n2
    combined.foreach(n => println(s"Result: $n"))
  }

  def demoZipWith(): Future[Int] = {
    val future1 = Future { getData1() }
    val future2 = Future { getData2() }
    val combined =  future1.zipWith(future2)(_ + _)
    combined
  }

  def demoRecover(): Future[Int] = {
    Future { persist(10) } recover { case e: SQLException => 0 }
  }

  def demoFailed() {
    val f = Future { persist(20)}
    for (ex <- f.failed) println(ex)
  }

  def dependentFutures(): Future[Int] = {
    def future1 = Future { getData1() }
    // the second step depends on the output of the first:
    def future2(arg: Int) = Future { getMoreData(arg) } // def, not val
    val combined = for (n1 <- future1; n2 <- future2(n1)) yield n1 + n2
    combined
  }

  def getMoreData(arg: Int): Int = {
    arg*10
  }

  def getData1(): Int = {
    10
  }

  def getData2(): Int = {
    20
  }

  def persist(arg: Int): Int = {
    throw new SQLException
  }
}

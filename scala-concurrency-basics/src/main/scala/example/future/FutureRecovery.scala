package example.future

import example.future.FutureRecovery.donutStock

import scala.concurrent.Future
import scala.util.{Failure, Success}

object FutureRecovery {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def donutStock(donut: String): Future[Int] = Future {
    if(donut == "vanilla donut") 10
    else throw new IllegalStateException("Out of stock")
  }

  def similarDonutStock(donut: String): Future[Int] = Future {
    println(s"replacing donut stock from a similar donut = $donut")
    if(donut == "vanilla donut") 20 else 5
  }

  def demoRecoverSuccess(): Unit = {
    donutStock("vanilla donut")
      .recover { case e: IllegalStateException if e.getMessage == "Out of stock" => 0 }
      .onComplete {
        case Success(donutStock)  => println(s"Results $donutStock")
        case Failure(e)           => println(s"Error processing future operations, error = ${e.getMessage}")
      }
  }

  //  Call Future.recover to recover from a known exception
  def demoRecoverFailure(): Unit = {
    println("\nStep 3: Call Future.recover to recover from a known exception")
    donutStock("unknown donut")
      .recover { case e: IllegalStateException if e.getMessage == "Out of stock" => 0 }
      .onComplete {
        case Success(donutStock)  => println(s"Results $donutStock")
        case Failure(e)           => println(s"Error processing future operations, error = ${e.getMessage}")
      }
  }

  // recoverWith() method  requires a return type of Future.
  // By using the recoverWith() method, we can continue the execution flow of our program.
  def demoRecoverWith(): Unit = {
    println("\nStep 3: Call Future.recoverWith to recover from a known exception")
    donutStock("unknown donut")
      .recoverWith { case e: IllegalStateException if e.getMessage == "Out of stock" => Future.successful(0) }
      .onComplete {
        case Success(donutStock)  => println(s"Results $donutStock")
        case Failure(e)           => println(s"Error processing future operations, error = ${e.getMessage}")
      }
  }

  def demoFallBackTo(): Unit = {
    println("\nStep 3: Call Future.fallbackTo")
    var donutStockOperation = ""
     donutStock("plain donut")
      .fallbackTo(similarDonutStock("vanilla donut"))
      .onComplete {
        case Success(donutStock)  => donutStockOperation= s"Results $donutStock"
        case Failure(e)           => donutStockOperation= s"Error processing future operations, error = ${e.getMessage}"
      }
    println(donutStockOperation)
  }
}

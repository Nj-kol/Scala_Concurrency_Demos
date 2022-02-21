package example.future

import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object FutureMethods {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def donutStock(donut: String): Future[Option[Int]] = Future {
    println("checking donut stock ... sleep for 2 seconds")
    Thread.sleep(2000)
    if(donut == "vanilla donut") Some(10) else None
  }

  def demoSequence(): Unit = {

    val x1 = Future {
      Thread.sleep(2000)
      println("x1 is completed")
      "Hello x1"
    }

    val x2 = Future {
      Thread.sleep(4000)
      println("x2 is completed")
      throw new Exception("Exception occured with x2")
    }

    val x3 = Future {
      Thread.sleep(10000)
      println("x3 is completed")
      "Hello x3"
    }

    val x4 = Future {
      Thread.sleep(10000)
      println("x3 is completed")
      "Hello x4"
    }

    // Case 1: compose parallel tasks with exception
    val x:  Future[List[String]] = Future.sequence(List(x1, x2, x3))

    x.onComplete {
      case Success(res) => println("Success: " + res)
      case Failure(ex)  => println("Ohhh Exception: " + ex.getMessage)
    }

    Await.ready(x, 20.seconds)

    // Case 1: compose parallel tasks without exception
    val y:  Future[List[String]] = Future.sequence(List(x1, x3, x4))

    y.onComplete {
      case Success(res) => println("Success: " + res)
      case Failure(ex)  => println("Ohhh Exception: " + ex.getMessage)
    }

    Await.ready(y, 20.seconds)
  }

  def demoTraverse(): Unit = {

    val futureOperations = List(
      donutStock("vanilla donut"),
      donutStock("plain donut"),
      donutStock("chocolate donut")
    )

    println(s"\nStep 3: Call Future.traverse to convert all Option of Int into Int")
    val futureTraverseResult = Future.traverse(futureOperations){
      futureSomeQty => futureSomeQty.map(someQty => someQty.getOrElse(0))
    }
    futureTraverseResult.onComplete {
      case Success(results) => println(s"Results $results")
      case Failure(e)       => println(s"Error processing future operations, error = ${e.getMessage}")
    }

    Await.ready(futureTraverseResult, 5.seconds)
  }

  def demoFoldLeft(): Unit = {

    println(s"\nStep 2: Create a List of future operations")
    val futureOperations = List(
      donutStock("vanilla donut"),
      donutStock("plain donut"),
      donutStock("chocolate donut"),
      donutStock("vanilla donut")
    )

    println(s"\nStep 3: Call Future.foldLeft to fold over futures results from left to right")
    val futureFoldLeft = Future.foldLeft(futureOperations)(0){ case (acc, someQty) =>
      acc + someQty.getOrElse(0)
    }

    futureFoldLeft.onComplete {
      case Success(results) => println(s"Results $results")
      case Failure(e)       => println(s"Error processing future operations, error = ${e.getMessage}")
    }

    Await.ready(futureFoldLeft, 5.seconds)
  }

  // Unlike foldLeft(), however, reduceLeft() does not allow you to provide a default value.
  def demoReduceLeft(): Unit = {

    val futureOperations = List(
      donutStock("vanilla donut"),
      donutStock("plain donut"),
      donutStock("chocolate donut"),
      donutStock("vanilla donut")
    )

    println(s"\nStep 3: Call Future.reduceLeft to fold over futures results from left to right")
    val futureFoldLeft = Future.reduceLeft(futureOperations){
      case (acc, someQty) => acc.map(qty => qty + someQty.getOrElse(0))
    }

    futureFoldLeft.onComplete {
      case Success(results) => println(s"Results $results")
      case Failure(e)       => println(s"Error processing future operations, error = ${e.getMessage}")
    }

   Await.ready(futureFoldLeft, 5.seconds)
  }

  def demofirstCompletedOf(): Unit = {

    println(s"\nStep 2: Create a List of future operations")
    val futureOperations = List(
      donutStock("vanilla donut"),
      donutStock("plain donut"),
      donutStock("chocolate donut"),
      donutStock("vanilla donut")
    )
    println(s"\nStep 3: Call Future.firstCompletedOf to get the results of the first future that completes")
    val futureFirstCompletedResult = Future.firstCompletedOf(futureOperations)

    futureFirstCompletedResult.onComplete {
      case Success(results) => println(s"Results $results")
      case Failure(e)       => println(s"Error processing future operations, error = ${e.getMessage}")
    }
    Await.ready(futureFirstCompletedResult, 5.seconds)
  }

  // Future.zip will create a new future whose return type will be a tuple holding the return types of the two futures.
  def demoZip(): Unit = {
    println(s"\nStep 2: Define a method which returns a Future Double for donut price")
    def donutPrice(): Future[Double] = Future.successful(3.25)

    println(s"\nStep 3: Zip the values of the first future with the second future")
    val donutStockAndPriceOperation = donutStock("vanilla donut") zip donutPrice()

    donutStockAndPriceOperation.onComplete {
      case Success(results) => println(s"Results $results")
      case Failure(e)       => println(s"Error processing future operations, error = ${e.getMessage}")
    }

    Await.ready(donutStockAndPriceOperation, 5.seconds)
  }

  // Similar to future zip() method, Scala also provides a handy future zipWith() method.
  // In addition to combining the results of two futures, the zipWith() method allows you to pass-through a function which can be applied to the results.
  def demoZipWith(): Unit = {

    def donutPrice(): Future[Double] = Future.successful(3.25)
    val qtyAndPriceF: (Option[Int], Double) => (Int, Double) = (someQty, price) => (someQty.getOrElse(0), price)

    val donutAndPriceOperation = donutStock("vanilla donut").zipWith(donutPrice())(qtyAndPriceF)

    donutAndPriceOperation.onComplete {
      case Success(result) => println(s"Result $result")
      case Failure(e)      => println(s"Error processing future operations, error = ${e.getMessage}")
    }
    Await.ready(donutAndPriceOperation, 5.seconds)
  }

  // andThen() is used whenever you have a need to apply a side-effect function on the result returned by the future.
  def demoAndThen(): Unit = {
    println(s"\nStep 2: Call Future.andThen with a PartialFunction")
    val donutStockOperation = donutStock("vanilla donut")
    donutStockOperation.andThen { case stockQty => println(s"Donut stock qty = $stockQty")}
  }

}

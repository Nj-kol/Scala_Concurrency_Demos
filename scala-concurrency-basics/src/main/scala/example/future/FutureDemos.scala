package example.future

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.math.random
import scala.util.{Failure, Success}

object FutureDemos {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def demoAwait(): Unit = {
    val f = Future {
      Thread.sleep(10000);
      42
    }

    val result = Await.result(f, 10.seconds)
    println(result)
  }

  def demoCallBack(): Unit = {
    val f = Future {
      Thread.sleep(10000)
      if (random() < 0.5) throw new Exception
      42
    }

    f.onComplete {
      case Success(v) => println(s"The answer is $v")
      case Failure(ex) => println(ex.getMessage)
    }

    // do something while the process completes
    for(i <- 1 to 10) {
      println("waiting to get the perfect random number")
      Thread.sleep(1000)
    }
  }

}

package example.future

import java.util.concurrent.Executors
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future, blocking}
import scala.io.Source

object BlockingFuturesDemo {

  def demo_Blocking_outside_the_Future() {

    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    val f = Future {
      Thread.sleep(10000);
      42
    }

    // Always have timeouts when blocking is necessary
    val result = Await.result(f, 10.seconds)
    println(result)
  }

  def demo_Blocking_inside_the_Future() {

    val pool = Executors.newCachedThreadPool()
    //  Now this pool is used by all futures when ec is in scope.
    implicit val ec = ExecutionContext.fromExecutor(pool)

    val f = Future {
      var contents:Option[String] = None
      blocking {
        for (empId <- 1 to 25)
          contents = Some(Source.fromURL(s"http://dummy.restapiexample.com/api/v1/employee/${empId}").mkString)
      }
      contents
    }(ec)
  }
}

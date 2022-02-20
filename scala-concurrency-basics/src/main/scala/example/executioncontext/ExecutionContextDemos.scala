package example.executioncontext

import java.util.concurrent.Executors
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future, blocking}
import scala.io.Source
import scala.util.{Failure, Success}

object ExecutionContextDemos {

  def demoCustomEc(): ArrayBuffer[String] = {

    val pool = Executors.newCachedThreadPool()
    //  Now this pool is used by all futures when ioPool is in scope.
    implicit val ioPool= ExecutionContext.fromExecutor(pool)

    val employees = new ArrayBuffer[String]()

    val f = Future {
      var contents:Option[String] = None
      blocking {
        for (empId <- 1 to 25)
         contents = Some(Source.fromURL(s"http://dummy.restapiexample.com/api/v1/employee/${empId}").mkString)
      }
      contents
    }(ioPool)

    f.onComplete {
      case Success(v) =>  employees+=v.getOrElse("Not found")
      case Failure(ex) => println(ex.getMessage)
    }
    employees
  }
}

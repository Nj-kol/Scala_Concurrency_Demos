package example.promise

import org.scalatest.funsuite.AnyFunSuite

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class PromiseSpec extends AnyFunSuite {

  test("runByPromise") {
    val fres = PromisesDemo.runByPromise(5+2)
    val result = Await.result(fres, 60.seconds)
    println(result)
  }

  test("gimmeMyPreciousValue success") {
    val fres = PromisesDemo.gimmeMyPreciousValue(12)
    val result = Await.result(fres, 10.seconds)
    println(result)
  }


  test("gimmeMyPreciousValue failure") {
    val fres = PromisesDemo.gimmeMyPreciousValue(1600)
    fres onComplete {
      case Success(value) => println(value)
      case Failure(exception) => println(exception.getMessage)
    }
    Await.result(fres, 10.seconds)
  }

}

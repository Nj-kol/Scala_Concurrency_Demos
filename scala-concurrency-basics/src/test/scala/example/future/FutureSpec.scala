package example.future

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime

import scala.concurrent.{Await, Future}

class FutureSpec extends AnyFunSuite {

  test("level 1 composition test") {
    val f  = ComposeFutures.composefuture1()
    val result = Await.result(f, 10.seconds)
    println(result)
  }

  test("level 2 composition test") {
    val f  = ComposeFutures.composefuture2()
    val result = Await.result(f, 10.seconds)
    println(result)
  }

  test("level 3 composition test") {
    val f  = ComposeFutures.composefuture2()
    val result = Await.result(f, 10.seconds)
    println(result)
  }

  test("dependent futures") {
    val f  = ComposeFutures.dependentFutures()
    val result = Await.result(f, 10.seconds)
    println(result)
  }

  test("recover") {
    val f  = ComposeFutures.demoRecover()
    val result = Await.result(f, 10.seconds)
    println(result)
  }

  test("foreach") {
    ComposeFutures.demoForEach
  }

  test("failed") {
    ComposeFutures.demoFailed
  }

}


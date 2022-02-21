package example.future

import org.scalatest.funsuite.AnyFunSuite

class FutureMethodSpec extends AnyFunSuite {

  test("sequence") {
    FutureMethods.demoSequence()
  }

  test("traverse") {
    FutureMethods.demoTraverse()
  }

  test("foldLeft") {
    FutureMethods.demoFoldLeft()
  }

  test("reduceLeft") {
    FutureMethods.demoReduceLeft()
  }

  test("demofirstCompletedOf") {
    FutureMethods.demofirstCompletedOf()
  }

  test("demo zip") {
    FutureMethods.demoZip()
  }

  test("demo zipWith") {
    FutureMethods.demoZipWith()
  }

  test("demo andThen") {
    FutureMethods.demoAndThen()
  }
}

package example.future

import org.scalatest.funsuite.AnyFunSuite

class FutureRecoverSpec extends AnyFunSuite {

  test("recover success - i.e. no excpetion") {
    FutureRecovery.demoRecoverSuccess()
  }

  test("recover failure - i.e. with excpetion") {
    FutureRecovery.demoRecoverFailure()
  }

  test("recover With") {
    FutureRecovery.demoRecoverWith()
  }

  test("fallback to") {
    FutureRecovery.demoFallBackTo()
  }
}

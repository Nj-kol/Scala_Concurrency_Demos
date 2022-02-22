package example.promise

import java.lang.RuntimeException
import scala.concurrent.{Future, Promise}
import scala.util.control.NonFatal
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

object PromisesDemo {

  def runByPromise[T](block: => T)(implicit ec: ExecutionContext): Future[T] = {

    // we create a promise
    val p = Promise[T]

    // In the middle, we submit a function which will be evaluated at some point, out of our control.
    // At that moment, the service produces the value and fulfils the Promise, which will automatically fulfil the Future for the consumer.
    ec.execute { () =>
      try {
        p.success(block)
      } catch {
        case NonFatal(e) => p.failure(e)
      }
    }

    // we return its future at the end, for whoever wants to consume it.
    p.future
  }

  def gimmeMyPreciousValue(yourArg: Int): Future[String] = {

    // create promise now
    val thePromise = Promise[String]()

    // submit a task to be evaluated later, at the discretion of the service
    // note: if the service is not on the same JVM, you can pass a tuple with the arg and the promise so the service has access to both
    Future{
      try {
        val res = MyService.produceThePreciousValue(yourArg)
        thePromise.success(res)
      } catch {
        case e: RuntimeException => thePromise.failure(e)
      }
    }
    // return the future now, so it can be reused by whoever's consuming it
    thePromise.future
  }
}

object MyService {
  def produceThePreciousValue(theArg: Int): String = {
    Thread sleep 5000
    if(theArg<100) {
      "The meaning of your life is " + (theArg / 4)
    }else{
      throw new RuntimeException("Your expectation is too high")
    }
  }
}
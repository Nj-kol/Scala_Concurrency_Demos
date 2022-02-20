package example

import example.executioncontext.ExecutionContextDemos

object Hello extends Greeting with App {

  val resp = ExecutionContextDemos.demoCustomEc()
  Thread.sleep(10000)
  resp.foreach(println)
}

trait Greeting {
  lazy val greeting: String = "hello"
}

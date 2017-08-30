import io.reactors._

class Schedulers extends Reactor[String] {
  var count = 1       // If changed use Thread.sleep
  sysEvents onMatch {
    case ReactorScheduled =>
      println("---------------------------Reactor is scheduled-------------------------")

    case ReactorPreempted =>

      count -= 1
      if (count == 0) {

        main.seal()
        println("--------------------------Reactor is terminating------------------------\n")
      }
  }
  main.events.onEvent(println)
}

object SchedulerObject {
  def main(args: Array[String]) {
    val system = new ReactorSystem("test-system")
    val proto = Proto[Schedulers].withScheduler(
      JvmScheduler.Key.globalExecutionContext)
    val ch = system.spawn(proto)      //for system event generation
    ch ! "------------------- Checking out how Scheduler Works First Time--------------------"           // for main.events generation
    //Thread.sleep(1000) // Uncomment if count > 1
    ch ! "------------------- Checking out how Scheduler Works Second Time-------------------"           // for main.events generation

   system.bundle.registerScheduler("customTimer",
      new JvmScheduler.Timer(5000))
    val periodic = system.spawn(
      Proto[Schedulers].withScheduler("customTimer"))
    periodic ! "---------------- Scheduler with Custom Timer of 5 seconds -------------------------"


    Thread.sleep(8000)
  }
}
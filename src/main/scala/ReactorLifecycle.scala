import io.reactors._
  /**
    *
    *        |
        V
    ReactorStarted
        |
        V
    ReactorScheduled <----
        |                 \
        V                 /
    ReactorPreempted -----
        |                 \
        |            ReactorDied
        V                 /
    ReactorTerminated <---
        |
        x


    */
  class ReactorLifecycle extends Reactor[String] {
    var first = true      //
    sysEvents onMatch {
      case ReactorStarted =>
        println("------------------------------Reactor Started--------------------------")
      case ReactorScheduled =>
        println("------------------------------Reactor Scheduled--------------------------")
      case ReactorPreempted =>
        println("------------------------------Reactor Preempted--------------------------")

        main.seal()   //comment to call termination manually in case of 2 calls

        if (first) first = false
        else throw new Exception(" ------------------------Exception Manually thrown----------------")
      case ReactorDied(_) =>
        println("------------------------------Reactor Dead--------------------------")
      case ReactorTerminated =>
        println("------------------------------Reactor Terminated--------------------------")

    }
  }




  object ReactorLifecycleObject {
    def main(args: Array[String]) {
      val system = new ReactorSystem("test-system")
      val ch = system.spawn(Proto[ReactorLifecycle])

      ch ! "event"
     // Thread.sleep(1000)
  //    ch ! "event"  //Uncomment to break the code and throw exception and show reactor dead

Thread.sleep(2000)
    }
  }

import io.reactors.{Reactor, ReactorSystem}
import io.reactors.protocol._

/**
  * Implement the sample in Chapter "2-Way Communication Protocol"
  * http://reactors.io/tutorialdocs//reactors/protocol-two-way/index.html
  *
  * xxxx
  * - xxxx
  * - xxxx
  */
object TwoWayProtocol {

  def main(args: Array[String]) {
    val system = new ReactorSystem("test-system")
println("---------------------------------TWO WAY COMMUNICATION PROTOCOL----------------------------------- ")
    val seeker = Reactor[Unit] { self =>
      val lengthServer = self.system.channels.twoWayServer[Int, String].serveTwoWay()

      lengthServer.links.onEvent { serverTwoWay =>
        serverTwoWay.input.onEvent { s =>
          serverTwoWay.output ! s.length
          print(s"\nServer Side : : : -------------Length recieved is ${s.length}-------------")
        }
      }

      lengthServer.channel.connect() onEvent { clientTwoWay =>
       val string = "What's my length?"
        println(s"\nClient Side : : : -----Sending a String to Server Side : $string -----")
        clientTwoWay.output ! string
        clientTwoWay.input onEvent { len =>
          if (len == 17) println("\n\nClient Side : : : ------ClientTwoWay received correct reply--------\n")
          else println("clientTwoWay reply incorrect: " + len)
        }
      }
    }

    system.spawn(seeker)
Thread.sleep(2000)
    System.out.println("Press any key to terminate")
    System.in.read()
    System.out.println("Shutting down reactors system...")
    system.shutdown()
  }
}
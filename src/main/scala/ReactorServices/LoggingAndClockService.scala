import io.reactors._
import io.reactors.services.Log

import scala.concurrent.Promise
import scala.concurrent.duration._

object LoggingAndClockService {
  def main(args: Array[String]) {
    val system = new ReactorSystem("test-system")

    val log = system.service[Log]
    val ch = system.spawn(Reactor[String] { self =>

      log.apply("\n--------------------- We have now started the Logging-----------------\n")
      self.main.seal()
    })
    ch ! "event"


    val done = Promise[Boolean]()
    val ch1 = system.spawn(Reactor[String] { self =>

      //  Emits an event after a timeout specified by the duration `d`
      system.clock.timeout(2.second) on {

        log.apply("\n---------------------Takes 2 second to print--------------------------\n")


      }

      system.clock.timeout(5.second) on {
        log.apply("\n---------------------Takes 5 second to print--------------------------")

        done.success(true)

      }

    })
    ch1 ! "event"
    Thread.sleep(8000)


  }
}

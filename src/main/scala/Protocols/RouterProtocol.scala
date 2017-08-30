import io.reactors._
import io.reactors.protocol._


object RouterProtocol {
  def main(args: Array[String]) {


    val system = ReactorSystem.default("test-system")

    val worker1 = system.spawnLocal[String] { self =>
      self.main.events.onEvent(x => println(s"---------------Event Recieved By Worker 1: ${x}-----------"))
    }
    val worker2 = system.spawnLocal[String] { self =>
      self.main.events.onEvent(x => println(s"---------------Event Recieved By Worker 2: ${x}-----------"))
    }

    system.spawnLocal[Unit] { self =>
      val router = system.channels.daemon.router[String]
        .route(Router.roundRobin(Seq(worker1, worker2))) //can be router.random / router.hash
      router.channel ! "one"
      router.channel ! "two"
      router.channel ! "three"
      router.channel ! "four"
    }
Thread.sleep(2000)
  }
}

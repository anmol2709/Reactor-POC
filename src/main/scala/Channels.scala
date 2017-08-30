import io.reactors.{Reactor, ReactorSystem, _}

import scala.collection._

trait Op[K, V]

case class Put[K, V](k: K, v: V) extends Op[K, V]

case class Get[K, V](k: K, ch: Channel[V]) extends Op[K, V]

class MapReactor[K, V] extends Reactor[Op[K, V]] {
  val map: mutable.Map[K, V] = mutable.Map[K, V]()
  main.events onEvent {
    case Put(k, v) => map(k) = v
    case Get(k, ch) => ch ! map(k)
  }
}

object Channels {
  def main(args: Array[String]) {
    val system = new ReactorSystem("test-system")

    val mapper: Channel[Op[String, List[String]]] = system.spawn(Proto[MapReactor[String, List[String]]])
    mapper ! Put("dns-main", "dns1" :: "lan" :: Nil)
    mapper ! Put("dns-backup", "" + "" :: "com" :: Nil)

    val clientReactorChannel: Channel[String] = system.spawn(Reactor[String] { self =>
      val log = system.log
      log.apply("Logger started")

      self.main.events onMatch {
        case "start" =>
          val reply: Connector[List[String]] = self.system.channels.daemon.open[List[String]] //contains channel and events
          log.apply("About to call Get")
          val key = "dns-main"
          mapper ! Get(key, reply.channel)
          reply.events onEvent { (url: Seq[String]) =>
            log.apply(s"Result list for key: $key is: $url")
          }
        case "end" =>
          self.main.seal()
      }
    })

    clientReactorChannel ! "start"
    Thread.sleep(3000)
    System.out.println("Press any key to terminate")
    System.in.read()
    System.out.println("Shutting down reactors system...")
    clientReactorChannel ! "end"
    system.shutdown()
  }
}

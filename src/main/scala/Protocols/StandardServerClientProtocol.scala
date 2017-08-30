import io.reactors.ReactorSystem
import io.reactors.protocol._


object StandardServerClientProtocol {

  def main(args: Array[String]) {
    val system = new ReactorSystem("test-system")

    //create and start a server reactor
    val server = system.server[Int, Int]{(state, x) =>
      //state.subscription.unsubscribe()
      println(s"The Current state Of the Server is  : $state")  //The state object for the server contains the Subscription object, which allows clients to stop the server if an unexpected event arrives.
      x * 2 }

// ? : Request a single reply from the server channel
    /**
      * Client basically querying the server and getting a response
      */
    system.spawnLocal[Unit] { self =>
      println("Quering the server for multiplying 2 with input number  : 7 \n ")
      (server ? 7) onEvent { response =>
        println(s"\n\nResponse from server  : : : : $response")
      }

    }

Thread.sleep(2000)
    System.out.println("\n Press any key to terminate")
    System.in.read()
    System.out.println("Shutting down reactors system...")
    system.shutdown()
  }
}
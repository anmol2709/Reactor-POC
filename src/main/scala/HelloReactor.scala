import io.reactors._


/**This program  declares an anonymous reactor called helloReactor,
  * which waits for a name to arrive on its main event stream,prints that name,
  * and then seals its main channel, therefore terminating itself.
  */


object HelloReactor  {
  def main(args: Array[String]) {
    val welcomeReactor = Reactor[String] {
      self =>
        self.main.events onEvent { evt =>
          println("\t-------------------HELLO---------------------")
          println("    \t\t\t\t ______" + "\n\t\t\t\t/  __  \\" + "\n\t\t\t       /  /  \\  \\" + "\n\t\t\t      /\t  \\__/   \\" + "\n\t\t\t     /\t  \t  \\" + "\n\t\t\t    /\t   _       \\" + "\n\t\t\t   /\t  |_|\t    \\" + "\n\t\t\t  /  __        __    \\" + "\n\t\t\t /  /  \\      /  \\    \\" + "\n\t\t\t \\  \\__/      \\__/    /" + "\n\t\t\t  \\__________________/ ")
          println(s"\n\t--------------- ${evt.toUpperCase}!!!!!!!!! ----------------")
          self.main.seal() //Terminates the reactor
        }


    }
    val system = ReactorSystem.default("test-system")
    val ch = system.spawn(welcomeReactor) //to start a specific reactor returning a default channel
    ch ! "Reactor"
    Thread.sleep(3000) // Required as IDE run Scala programs in a separate JVM process
    //  to ensure that this new JVM process does not die when the main function ends
  }
}


package Events

import io.reactors._

object EventReactor {
  def main(args: Array[String]): Unit = {

    val myEvents: Events[String] = new Events.Emitter[String] //produces string events
    /**
      * method : onEvent  :: allows users to manipulate the events
      * parameters : takes a user callback function
      */
   /* def trace[T](events: Events[T]) {
      events.onEvent(x=>println(x))
    }*/
    val emitter = new Events.Emitter[Int]

    var luckyNumber = 0


    //    Using Event Streams as Collection In Scala/ Streams In Java

    /*val eventFunctionality =emitter.filter(x=>x>8)
    eventFunctionality.onEvent(value =>print(value))*/



    emitter.onEvent{
      value => luckyNumber = value //reassigning luckyNumber to 7 on Event call

      if(luckyNumber == 7)
         println(s"---------------The selected number is  $luckyNumber : Congrats You are lucky!!!!!!!-------------")
         else
         println(s"The selected number is  $luckyNumber :Sorry You are not so lucky!!!!!!")

    }


    for(i<-0 to 10) yield emitter.react(i) //'tells' (!) to produce events

     // Throws expection if wrong

  Thread.sleep(5000)
  }
}

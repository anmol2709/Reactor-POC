package Events

import io.reactors._

object HigherOrderEventStreams {
  def main(args: Array[String]) {
    var seen = List[Int]()
    val higherOrder = new Events.Emitter[Events[Int]]

//2 streams
    val evens = new Events.Emitter[Int]
    val odds = new Events.Emitter[Int]


    higherOrder.mux.onEvent(seen ::= _)
    //This operator multiplexes events from the last Events[T] – whenever a new nested event stream is emitted,
    // events from previously emitted nested event streams are ignored,
    // and only the events from the latest nested event stream get forwarded.

    evens react 2
    println("----------------Trying to add 2 -  Elements in the List Seen :: " + seen)
    odds react 1
    println("----------------Trying to add 1 -  Elements in the List Seen :: " + seen)

    higherOrder react evens  //only evens react will be added after this
    println("--------- After this only even reacts will be added --------")

    odds react 1
    println("----------------Trying to add 1 -  Elements in the List Seen :: " + seen)

    assert(seen == Nil)


    evens react 4           //added
    println("----------------Trying to add 4 -  Elements in the List Seen :: " + seen)

    assert(seen ==  4 :: Nil)

    higherOrder react odds //only odds react will be added after this
    println("--------- After this only odd reacts will be added --------")

    evens react 6

    println("----------------Trying to add 6 -  Elements in the List Seen :: " + seen)
    odds react 5      //added

    println("----------------Trying to add 5 -  Elements in the List Seen :: " + seen)
    assert(seen == 5  :: 4 :: Nil)

    var seen2 = List[Int]()
    val higherOrder2 = new Events.Emitter[Events[Int]]
    println("\n\n--------- Trying to use Union --------")

    higherOrder2.union.onEvent(seen2 ::= _)
    // Union to obtain all the events from all the even streams produced by the higher-order event stream
    higherOrder2 react evens //only evens after this
    println("--------- After this only even reacts will be added --------")
    odds react 3
    println("----------------Trying to add 3 -  Elements in the List Seen2 :: " + seen2)

    evens react 4 //added
    println("----------------Trying to add 4 -  Elements in the List Seen :: " + seen2)

    assert(seen2 == 4 :: Nil)
    higherOrder2 react odds //both even and odds after this as union
    println("--------- After this only even and odd reacts will be added --------")

    evens react 6 //added
    println("----------------Trying to add 6 -  Elements in the List Seen :: " + seen2)

    assert(seen2 == 6 :: 4 :: Nil)
    odds react 5 //added
    println("----------------Trying to add 5 -  Elements in the List Seen :: " + seen2)

    assert(seen2 == 5 :: 6 :: 4 :: Nil)


  }

}

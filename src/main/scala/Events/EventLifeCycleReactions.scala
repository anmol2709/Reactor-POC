package Events

import io.reactors._

object EventLifeCycleReactions {
  def main(args: Array[String]): Unit = {
    var seen = List[Int]()
    var errors = List[String]()
    var done = 0
    val e = new Events.Emitter[Int]
    e.onReaction(new Observer[Int] {
      def react(x: Int, hint: Any) = seen ::= x
      def except(t: Throwable) = errors ::= t.getMessage
      def unreact() = done += 1
    })

    e.react(1)
    assert(seen == 1 :: Nil)
    e.react(2)
    assert(seen == 2 :: 1 :: Nil)
    assert(done == 0)
    e.except(new Exception("^_^"))
    assert(errors == "^_^" :: Nil)
    e.react(3)
    assert(seen == 3 :: 2 :: 1 :: Nil)
    assert(done == 0)
    e.unreact()
    assert(done == 1)
    e.react(4) // of no meaning as after unreact
    e.except(new Exception("o_O")) // won't count as after unreact
    assert(seen == 3 :: 2 :: 1 :: Nil) //4 not added as after unreact
    assert(errors == "^_^" :: Nil)   // true as new exception after unreact
    assert(done == 1)

  }

}

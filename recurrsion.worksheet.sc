val list = List(List("A", "B", "C"), List("D", "E", "F"))
def mergeList(source: List[List[String]]): List[String] = {
  if (source.isEmpty) return List()
  source(0) ++ mergeList(source.slice(1, source.length))
}

val list2 = List(List("A", "B", "C"))
mergeList(list2)
val list3 = list2.slice(1, list2.length)
list3.isEmpty
mergeList(list3)
mergeList(list)

def increase(n: Int, list: List[Int]): List[Int] = {
  @annotation.tailrec
  def loop(n: Int, list: List[Int], sum: List[Int]): List[Int] = {
    if (list.isEmpty) return sum
    loop(n, list.tail, sum :+ list.head + n)
  }
  loop(n, list, List())
}

val l = List(1, 2, 3, 4, 5)
val l0 = List(1)
increase(1, l0)
increase(1, l)

def multiply(n: Int, list: List[Int]): List[Int] = {
  @annotation.tailrec
  def loop(n: Int, list: List[Int], sum: List[Int]): List[Int] = {
    if (list.isEmpty) sum
    else loop(n, list.tail, sum :+ list.head * n)
  }
  loop(n, list, List())
}

multiply(1, l0)
multiply(2, l)

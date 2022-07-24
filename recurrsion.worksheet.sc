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

def increase = applyAll(add) _
val l = List(1, 2, 3, 4, 5)
val l0 = List(1)
increase(1)(l0)
increase(1)(l)

def applyAll(func: (Int, Int) => Int)(n: Int)(list: List[Int]): List[Int] = {
  @annotation.tailrec
  def loop(
      func: (Int, Int) => Int,
      n: Int,
      list: List[Int],
      sum: List[Int]
  ): List[Int] = {
    if (list.isEmpty) sum
    else loop(func, n, list.tail, sum :+ func(list.head, n))
  }
  loop(func, n, list, List())
}

multiplyAll(1)(l0)
multiplyAll(2)(l)

def multiplyAll = applyAll(multiply) _
def multiply(a: Int, b: Int) = a * b
def add(a: Int, b: Int) = a + b

def multiplyTwo = multiplyAll(2)
def addFive = increase(5)

(multiplyTwo andThen addFive)(l)

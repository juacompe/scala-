println("Hello World")
1 + 1

val x = 0
var y = 1

y = 9

if (x < 0) println("Negative")
else if (x > 0) println("Positive")
else println("Zero")

val listOfInt = List(1, 2, 3, 4, 5)

for (element <- listOfInt) println(element)
listOfInt.foreach(element => println(element))

val result =
  for {
    e <- listOfInt
    if e >= 3
  } yield e

listOfInt
result

import scala.io.Source

val urls = for (line <- Source.fromFile("source.txt").getLines) yield line
println(urls)

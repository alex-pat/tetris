package by.alexpat.tetris

class ScalaExtensions {
  def qsort(array: Array[Int], left: Int, right: Int) {
    val pivot = array((left + right) / 2)
    var i = left
    var j = right
    while (i <= j) {
      while (array(i) < pivot) i += 1
      while (array(j) > pivot) j -= 1
      if (i <= j) {
        val temp = array(i)
        array(i) = array(j)
        array(j) = temp
        i += 1
        j -= 1
      }
    }
    if (left < j) qsort(array, left, j)
    if (i < right) qsort(array, i, right)
  }
}
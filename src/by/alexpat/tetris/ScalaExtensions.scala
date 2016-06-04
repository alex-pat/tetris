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

  def getActionSequencesCount(xs: Array[Array[String]], pattern: String): Int = {
    val index = if (pattern(0) == 'T' ) 0 else 1
    val action = pattern(index)
    val seq = for (i <- 0 until xs.length) yield
                for (j <- 0 until xs(i).length if xs(i)(j)(index) == action) yield action
    seq.map(_.length).sum
  }

  def getLeftCount(array: Array[Array[String]]): Int =
    getActionSequencesCount(array, "KA")

  def getRightCount(array: Array[Array[String]]): Int =
    getActionSequencesCount(array, "KD")

  def getRotatesCount(array: Array[Array[String]]): Int =
    getActionSequencesCount(array, "KR")

  def getTetraminoesCount(array: Array[Array[String]]): Int =
    getActionSequencesCount(array, "T")

  def getPseudocode(actions: Array[String]): String = {

    def getStringFromAction(action: String): String =
      action match {
        case "KA" => "Tetramino was moved to the left\n"
        case "KD" => "Tetramino was moved to the right\n"
        case "KR" => "Tetramino was rotated\n"
        case a => "Tetramino was dropped\nThe \"" + a.charAt(1) + "\" tetramino was spawned\n"
      }

    val strings = actions.toList.map(getStringFromAction(_))
    val res = strings.mkString
    res.substring(res.indexOf("\n") + 1)
  }

}
fun main() {
    print("Enter size of list: ")
    val n: Int
    try {
        n = saveReadLine().toInt()
    } catch (e: NumberFormatException) {
        println("Not number")
        return
    }
    val list = mutableListOf<String>()
    repeat(n) {
        list.add(saveReadLine())
    }
    println("Result: " + getAlphabet(list))
}

private fun saveReadLine(): String {
    var result = readLine()
    while (result.isNullOrBlank()) result = readLine()
    return result
}

private var isPossible = true

/**
 * This function has 2 stages:
 *      1. transforms input list of surnames in tree of chars (used recursive call of fun transformToNodes);
 *      2. makes breadth-first tree traversal to merge all nodes.
 *
 * On 2 stage the function takes all the descendant lists at the given level and separately tries to merge
 * with the lists already considered (tries to find intersection of lists and then slice new list and insert
 * in old list). Also here it checks for mismatches in intersected char sequences.
 */
fun getAlphabet(list: List<String>): String {

    isPossible = true
    var result = "Impossible"
    val tree = transformToNodes(list)

    if (isPossible) {

        val seq = mutableListOf<List<Char>>()
        var nodeGroupList = listOf(tree)

        while (isPossible && nodeGroupList.isNotEmpty()) {
            for (nodeList in nodeGroupList) {

                var charSequence = nodeList.map { it.c }

                if (charSequence.toSet().size < charSequence.size) {
                    isPossible = false
                    break
                }

                var i = 0
                while (i < seq.size) {

                    val tuple = seq[i]
                    val intersectIndices = charSequence.map { tuple.indexOf(it) }.filter { it != -1 }

                    if (intersectIndices != intersectIndices.sorted()) {
                        isPossible = false
                        break
                    }

                    if (intersectIndices.isNotEmpty()) {

                        val slicedSeq = mutableListOf<List<Char>>()
                        val indices = intersectIndices.map { charSequence.indexOf(tuple[it])+1 }
                        var prev = 0

                        for (j in indices) {
                            val sublist = if (j == indices.last())
                                charSequence.subList(prev, charSequence.size)
                            else
                                charSequence.subList(prev, j)

                            slicedSeq.add(sublist)
                            prev = j
                        }

                        charSequence = tuple.toMutableList()
                        seq.remove(tuple)
                        for (j in intersectIndices.indices) {
                            charSequence.removeAt(intersectIndices[j])
                            charSequence.addAll(intersectIndices[j], slicedSeq[j])
                        }
                    } else i++
                }
                if (!isPossible) break

                seq.add(charSequence)
            }
            if (!isPossible) break

            nodeGroupList = nodeGroupList.flatMap { it.filterIsInstance<ParentNode>().map { node -> node.children } }
        }
        if (isPossible) result = seq.flatten().joinToString()
    }
    return result
}

private fun transformToNodes(list: List<String>): List<Node> {
    return transformToNodes(list, 0)
}

/**
 * This function transform list of surnames in tree of chars (Using class Node as wrapper).
 * If there are several surnames starting with the same letter, node will be instance of
 * ParentNode and it will contains children (so until all descendants at the same level are
 * unique).
 *
 * If some word is the beginning of another, it must come first, otherwise the flag isPossible
 * will be set to false and the result will be returned immediately.
 */
private fun transformToNodes(list: List<String>, index: Int): List<Node> {

    val localList = mutableListOf<String>()
    val nodeList = mutableListOf<Node>()
    val listIterator = list.iterator()
    var currentChar: Char? = null

    while (isPossible && listIterator.hasNext()) {

        val name = listIterator.next().toLowerCase()

        if (index >= name.length) {
            if (currentChar == null)
                continue
            else {
                isPossible = false
                break
            }
        }

        val char = name[index]

        if (currentChar?.equals(char) != true) {
            if (currentChar != null) {
                if (localList.size > 1) {
                    val nodeChildren = transformToNodes(localList, index + 1)
                    nodeList.add(ParentNode(currentChar, nodeChildren))
                } else {
                    nodeList.add(Node(currentChar))
                }
                localList.clear()
            }

            currentChar = char
        }
        localList.add(name)
    }

    if (localList.isNotEmpty() && currentChar != null) {
        if (localList.size > 1) {
            val nodeChildren = transformToNodes(localList, index + 1)
            nodeList.add(ParentNode(currentChar, nodeChildren))
        } else {
            nodeList.add(Node(currentChar))
        }
    }

    return nodeList
}

private class ParentNode(c: Char, val children: List<Node>) : Node(c)

private open class Node(val c: Char)



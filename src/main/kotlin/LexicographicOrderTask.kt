private var isPossible = true

/**
 * This function has 2 stages:
 *      1. transforms input list of surnames in tree of chars (used recursive call of fun transformToNodes;
 *      2. makes breadth-first tree traversal to merge all nodes.
 * After all it autofill result with missing letters.
 *
 * P.s. I apologize for the not very detailed description, I ran out of time.
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



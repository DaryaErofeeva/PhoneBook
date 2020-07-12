package phonebook

import java.io.File
import kotlin.math.sqrt

fun main() {
    val phones = File("directory.txt").useLines { it.toList() }
    val search = File("find.txt").useLines { it.toList() }

    println("Start searching (linear search)...")
    val linearResult = linearSearch(phones, search)
    printSearchResult(search.count(), linearResult.second, linearResult.first)

    println("\nStart searching (bubble sort + jump search)...")
    bubbleSortSearch(phones, search, linearResult.first)

    println("\nStart searching (quick sort + binary search)...")
    quickSortBinarySearch(phones, search)

    println("\nStart searching (hash table)...")
    hashTableSearch(phones, search)
}

fun printSearchResult(entriesCount: Int, found: Int, timeTaken: Long) {
    println("Found $found / $entriesCount entries. Time taken: ${formatMillis(timeTaken)}")
}

fun formatMillis(millis: Long): String {
    val min = millis / 1000 / 60
    val sec = millis / 1000 % 60
    val ms = millis % 1000

    return "$min min. $sec sec. $ms ms."
}

fun linearSearch(phones: List<String>, search: List<String>): Pair<Long, Int> {
    val startTime = System.currentTimeMillis()

    var found = 0
    for (s in search) {
        for (p in phones) {
            if (s == p.substringAfter(' ')) {
                found++
            }
        }
    }

    val timeTaken = System.currentTimeMillis() - startTime

    return timeTaken to found
}

fun bubbleSortSearch(phones: List<String>, search: List<String>, linearTimeTaken: Long) {
    val startTime = System.currentTimeMillis()
    val sortedPhones = phones.toMutableList()
    var fastEnough = true
    var sortTimeTaken = 0L

    var emptyGlass: String
    for (i in 0 until sortedPhones.count()) {
        for (j in 0 until sortedPhones.count() - i) {
            sortTimeTaken = System.currentTimeMillis() - startTime
            if (linearTimeTaken < 10 * sortTimeTaken) {
                fastEnough = false
                break
            }
            if (sortedPhones[i].substringAfter(' ') > sortedPhones[j].substringAfter(' ')) {
                emptyGlass = sortedPhones[i]
                sortedPhones[i] = sortedPhones[j]
                sortedPhones[j] = emptyGlass
            }
        }
    }

    val (searchResult, skippingLine) = if (fastEnough) {
        jumpSearch(sortedPhones, search, sortTimeTaken) to ""
    } else {
        linearSearch(sortedPhones, search) to " - STOPPED, moved to linear search"
    }

    printSearchResult(search.count(), searchResult.second, searchResult.first + sortTimeTaken)

    println("Sorting time: ${formatMillis(sortTimeTaken)}$skippingLine")
    println("Searching time: ${formatMillis(searchResult.first)}")
}

fun jumpSearch(phones: List<String>, search: List<String>, sortTimeTaken: Long): Pair<Long, Int> {
    val startTime = System.currentTimeMillis()

    var found = 0
    val loopStep = sqrt(phones.count().toDouble()).toInt()
    for (s in search) {
        for (i in 0 until phones.count() step loopStep) {
            if (s == phones[i].substringAfter(' ')) {
                found++
                continue
            }
            if (s < phones[i].substringAfter(' ')) {
                for (j in i downTo i - loopStep) {
                    if (s == phones[j].substringAfter(' ')) {
                        found++
                        break
                    }
                }
            }
        }
    }

    val timeTaken = System.currentTimeMillis() - startTime

    return timeTaken to found
}

fun quickSortBinarySearch(phones: List<String>, search: List<String>) {
    val startSortTime = System.currentTimeMillis()
    val sortedPhones = quickSort(phones)
    val sortTimeTaken = System.currentTimeMillis() - startSortTime

    val startSearchTime = System.currentTimeMillis()
    var found = 0
    for (query in search) {
        if (binarySearch(sortedPhones, query)) {
            found++
        }
    }
    val searchTimeTaken = System.currentTimeMillis() - startSearchTime
    printSearchResult(search.count(), found, searchTimeTaken + sortTimeTaken)

    println("Sorting time: ${formatMillis(sortTimeTaken)}")
    println("Searching time: ${formatMillis(searchTimeTaken)}")
}

fun quickSort(phones: List<String>): List<String> {
    if (phones.size < 2) {
        return phones
    }

    val left = mutableListOf<String>()
    val right = mutableListOf<String>()

    val pivotIndex = phones.size - 1
    val pivot = phones[pivotIndex]

    for (i in 0 until pivotIndex) {
        val phone = phones[i]
        if (phones[i].substringAfter(' ') <= pivot.substringAfter(' ')) {
            left.add(phone)
        } else {
            right.add(phone)
        }
    }

    return quickSort(left) + pivot + quickSort(right)
}

fun binarySearch(phones: List<String>, query: String): Boolean {
    if (phones.isEmpty()) {
        return false
    }

    val middleIndex = (phones.size - 1) / 2
    val middle = phones[middleIndex].substringAfter(' ')

    return when {
        middle > query -> binarySearch(phones.subList(0, middleIndex), query)
        middle < query -> binarySearch(phones.subList(middleIndex + 1, phones.size), query)
        else -> true
    }
}

fun hashTableSearch(phones: List<String>, search: List<String>) {
    val startCreatingTime = System.currentTimeMillis()
    val phonesTable = hashMapOf<String, String>()
    phones.forEach {
        phonesTable[it.substringAfter(' ')] = it.substringBefore(' ')
    }
    val creatingTimeTaken = System.currentTimeMillis() - startCreatingTime

    val startSearchTime = System.currentTimeMillis()
    var found = 0
    for (query in search) {
        if (phonesTable[query] != null) {
            found++
        }
    }
    val searchTimeTaken = System.currentTimeMillis() - startSearchTime
    printSearchResult(search.count(), found, searchTimeTaken + creatingTimeTaken)

    println("Creating time: ${formatMillis(creatingTimeTaken)}")
    println("Searching time: ${formatMillis(searchTimeTaken)}")
}

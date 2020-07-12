import java.util.Scanner
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val scanner = Scanner(System.`in`)

    val v1 = listOf(scanner.nextDouble(), scanner.nextDouble())
    val v2 = listOf(scanner.nextDouble(), scanner.nextDouble())

    val v1v2 = v1[0] * v2[0] + v1[1] * v2[1]

    val v1Length = sqrt(v1[0].pow(2) + v1[1].pow(2))
    val v2Length = sqrt(v2[0].pow(2) + v2[1].pow(2))

    val vCos = v1v2 / (v1Length * v2Length)

    println(Math.toDegrees(acos(vCos)).toInt())
}
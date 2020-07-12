import java.util.Scanner
import kotlin.math.floor

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val a = scanner.nextDouble()
    println(((a - floor(a)) * 10).toInt())
}
import kotlin.system.exitProcess
import kotlinx.coroutines.*

// Represents the current state of the game, including the secret number and the number of attempts.
data class GameState(val secretNumber: Int, var attempts: Int = 0)

// Sealed class to represent the different outcomes of a user's guess.
sealed class GuessResult {
    object Low : GuessResult()
    object High : GuessResult()
    object Correct : GuessResult()
}

fun GameState.printResult(result: GuessResult) {
    when (result) {
        GuessResult.Low -> {
            val tooLowStatements = setOf(
                "Too low? You need a telescope for your guesses!",
                "Even my cat could guess higher.",
                "You need a ladder for that guess!"
            )
            printWithAnimation(tooLowStatements.random())
        }

        GuessResult.High -> {
            val tooHighStatements = setOf(
                "You're reaching for the stars!",
                "Higher than a kangaroo's jump!",
                "That's like trying to catch a comet!",
                "You're closer to the moon than the correct number!",
            )
            printWithAnimation(tooHighStatements.random())
        }

        GuessResult.Correct -> {
            val correctStatements = setOf(
                "Congratulations! You guessed it!",
                "You're a mind reader!",
                "Spot on! You're a pro."
            )
            printWithAnimation(correctStatements.random())
        }
    }
}

fun printWithAnimation(message: String) {
    // Added sliding animation while displaying the result
    message.forEachIndexed { index, char ->
        print(char)
        Thread.sleep(50)
    }
    println()
}

fun main() {
    // ASCII art was generated online
    val welcomeBanner = """
:'######::'########::'######::'########::'########:'########::::'##::: ##:'##::::'##:'##::::'##:'########::'########:'########::
'##... ##: ##.....::'##... ##: ##.... ##: ##.....::... ##..::::: ###:: ##: ##:::: ##: ###::'###: ##.... ##: ##.....:: ##.... ##:
 ##:::..:: ##::::::: ##:::..:: ##:::: ##: ##:::::::::: ##::::::: ####: ##: ##:::: ##: ####'####: ##:::: ##: ##::::::: ##:::: ##:
. ######:: ######::: ##::::::: ########:: ######:::::: ##::::::: ## ## ##: ##:::: ##: ## ### ##: ########:: ######::: ########::
:..... ##: ##...:::: ##::::::: ##.. ##::: ##...::::::: ##::::::: ##. ####: ##:::: ##: ##. #: ##: ##.... ##: ##...:::: ##.. ##:::
'##::: ##: ##::::::: ##::: ##: ##::. ##:: ##:::::::::: ##::::::: ##:. ###: ##:::: ##: ##:.:: ##: ##:::: ##: ##::::::: ##::. ##::
. ######:: ########:. ######:: ##:::. ##: ########:::: ##::::::: ##::. ##:. #######:: ##:::: ##: ########:: ########: ##:::. ##:
:......:::........:::......:::..:::::..::........:::::..::::::::..::::..:::.......:::..:::::..::........:::........::..:::::..::""".trimIndent()

    println(welcomeBanner)
    println("\nWelcome to the Secret Number Guessing Game!\n")

    // Generate a random secret number between 1 and 100.
    val secretNumber = (1..100).random()

    // Create a new game state with the generated secret number.
    val gameState = GameState(secretNumber)

    println("Let's get started!\n")

    var result: GuessResult? = null
    do {
        print("Enter your guess: ")

        result = try {
            val guess = readLine()?.toInt() ?: throw NumberFormatException("Invalid input")
            evaluateGuess(gameState, guess)
        } catch (e: NumberFormatException) {
            println("Invalid input. Please enter a valid number.")
            continue
        }

        result?.let {
            gameState.printResult(result)
            gameState.attempts++
        } ?: println("We do not have result yet.")
    } while (result != GuessResult.Correct)

    println("Thanks for playing! Exiting...")
    exitProcess(0)
}

fun evaluateGuess(gameState: GameState, guess: Int): GuessResult = when {
    guess < gameState.secretNumber -> GuessResult.Low
    guess > gameState.secretNumber -> GuessResult.High
    else -> GuessResult.Correct
}

package furhatos.app.templateadvancedskill.responses

import furhatos.flow.kotlin.Utterance
import furhatos.flow.kotlin.utterance
import furhatos.gestures.Gestures

/**
 * Tip!
 * Define phrases elsewhere (here) to de-clutter the flow and keep all the dialogue in one place.
 * Useful for when creating multi-language skills, or skills with multiple personas with the same script.
 *
 * See furhatos.app.templateadvancedskill.responses.phrases for an example.
 * furhat.say(phrases.feelGoodUtterance)
 */

class Phrases {
    val Q_GoOnPowerBreak = listOf(
        "Did you want me to go on a power break?",
        "Do you want me to take a power nap? ",
        "Is it time for a power nap? ",
        "Should I take a short Power break?"
    ).random() // Include variance in phrasing without cluttering the flow.
    val Q_NapTimeOver = listOf(
        "Is nap time over?",
        "Is the nap over?",
        "Is the time for napping over?",
        "Is it time to stop napping?"
    ).random() // Include variance in phrasing without cluttering the flow.
    val A_feelGoodUtterance: Utterance =
        utterance { // Define complete and more complex utterances to get more variance and rich expressions without cluttering the flow.
            +"I feel"
            random {
                +"good"
                +"pretty good"
            }
            +Gestures.BigSmile
        }
    val A_FourYearPlanFallY1: Utterance = utterance {
        +"You will take six classes CPSC 130 Introduction to Computing and Programming, ENGL 102 Critical Writing, ESAP 101, FYRST Seminar, your choice of Intermediate algebra or Science of Life, Foundations of Academic Discovery, and you can pick a class that has a Creative and Aesthetic Inquiry."
    }
    val A_ComputerScience: Utterance = utterance {
        +"Computer science is the study of computers and computational systems."
        +"It includes programming, algorithms, and artificial intelligence."
        +Gestures.Smile
    }

    val A_CompSciDept: Utterance = utterance {
        +"Slippery Rock University has Department of Computer Science which offers majors and minors in Computing and Cybersecurity. Computing with three concentrations: Computer Science, Computing Analytics, and Information Technology."
        +Gestures.Smile
    }
    val A_CSMajor: Utterance = utterance {
        +"The Computing Major has three concentrations: Computer Science, Computing Analytics, and Information Technology."
    }

    val A_CyberMajor: Utterance = utterance {
        +"The Cybersecurity major has two concentrations: Secure Software Development and Security Governance."
    }
    val A_CyberMinors: Utterance = utterance {

    }
    val A_CSDegree: Utterance = utterance {

    }
    val A_CyberSecurityDegree: Utterance = utterance {}
}
val phrases = Phrases()
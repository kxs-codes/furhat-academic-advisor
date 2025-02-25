package furhatos.app.templateadvancedskill.flow.how_are_you

import furhatos.app.templateadvancedskill.nlu.*
import furhatos.flow.kotlin.*
import furhatos.util.Language
import furhatos.app.templateadvancedskill.responses.phrases

/**
 * Tip!
 *
 * This subflow has all relevant resources including intents, entities and phrases together in its own package
 * without any dependencies to other skills files. This allows for easy reuse of the subflow in other skills.
 */

/**
 * Example of a subflow
 * Flow will ask about how to user feels today and return.
 **/
val HowAreYou: State = state {
    onEntry {
        furhat.ask("What would you like to talk about?")
    }

    onResponse<ComputerScienceIntent> {
        furhat.say(phrases.A_ComputerScience)
        terminate()
    }
    onResponse<DepartmentIntent>{
        furhat.say(phrases.A_CompSciDept)
        terminate()
    }
    onResponse<CsMajorIntent> {
        furhat.say(phrases.A_CSMajor)
        terminate()
    }
    onResponse<CyberMajorIntent> {
        furhat.say(phrases.A_CyberMajor)
        terminate()
    }
    onResponse {
        furhat.say("I'm not sure about that. But I can tell you about computer science, cybersecurity, or the Computing four-year study plan. What would you like to know?")
        terminate()
    }

    onNoResponse {
        furhat.say("I didn't hear anything. Could you repeat?")
        terminate()
    }
}

/** Run this to test the intents of this state from the command line. **/
fun main(args: Array<String>) {
    while (true) {
        val utterance = readLine()
        val results = HowAreYou.getIntentClassifier(lang = Language.ENGLISH_US).classify(utterance!!)
        if (results.isEmpty()) {
            println("No match")
        } else {
            results.forEach {
                println("Matched ${it.intents} with ${it.conf} confidence")
            }
        }
    }
}
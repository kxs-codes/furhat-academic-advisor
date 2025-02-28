package furhatos.app.templateadvancedskill.flow.main

import furhatos.app.templateadvancedskill.flow.Parent
import furhatos.app.templateadvancedskill.flow.log
import furhatos.app.templateadvancedskill.nlu.*
import furhatos.app.templateadvancedskill.responses.gestures.hearSpeechGesture
import furhatos.app.templateadvancedskill.setting.AutoGlanceAway
import furhatos.app.templateadvancedskill.setting.AutoUserAttentionSwitching
import furhatos.app.templateadvancedskill.setting.beActive
import furhatos.app.templateadvancedskill.setting.isAttended
import furhatos.flow.kotlin.*
import furhatos.nlu.common.Greeting
import furhatos.app.templateadvancedskill.responses.phrases
import furhatos.app.templateadvancedskill.nlu.ConfirmIntent

/**
 * State where Furhat engage actively with the user.
 * Start your interaction from here.
 */
var lastHeard: String? = null
val discussedTopics = mutableListOf<String>()
val Active: State = state(Parent) {

    onEntry {
        furhat.beActive()
        log.debug("now I'm listening")

        // We're leaving the initiative to the user and extending the listen timeout from default 5000 ms to 8000 ms.
        furhat.listen(8000)
    }
    onReentry {
        when {
            !users.hasAny() -> goto(Idle)
            !furhat.isAttended() -> goto(WaitingToStart)
            else -> furhat.listen()
        }
    }
    include(AutoUserAttentionSwitching) // Switch user after a while
    include(AutoGlanceAway) // Glance away after some time of eye contact

    /** Handle simple meet and greet conversation **/
    // Handle multiple intents where one part of the intent was a Greeting
    onPartialResponse<Greeting> {
        furhat.attend(it.userId) // attend the user that spoke
        goto(GreetUser(it))
    }
    onResponse(listOf(Greeting(), HowAreYouIntent(), NiceToMeetYouIntent())) {
        furhat.attend(it.userId) // attend the user that spoke
        goto(GreetUser(it))
    }
    onResponse<ComputerScienceIntent> {
        furhat.attend(it.userId)
        furhat.say(phrases.A_ComputerScience)
        furhat.listen() // Keep the conversation open
    }
    onResponse<DepartmentIntent> {
        furhat.attend(it.userId)
        furhat.say(phrases.A_CompSciDept)
        furhat.listen() // Keep the conversation open
    }

    onResponse<FourYearPlanFY1Intent> {
        furhat.attend(it.userId)
        furhat.say(phrases.A_FourYearPlanFallY1)
        furhat.listen()
    }
    onResponse<CsMajorIntent> {
        furhat.attend(it.userId)
        furhat.say(phrases.A_CSMajor)
        furhat.listen()
    }
    onResponse<CyberMajorIntent> {
        furhat.attend(it.userId)
        furhat.say(phrases.A_CyberMajor)
        furhat.listen()
    }
    onResponse {
        furhat.attend(it.userId)
        lastHeard = it.text // Storing what the user says
        furhat.say("Did you say '${it.text}'?")
        goto(ConfirmUnderstanding)
    }

    /** Handle no response **/
    onNoResponse {
        furhat.say("I didn't hear anything. Could you repeat?")
        furhat.listen()
    }
    onResponse {
        // On unknown response, the robot reacts to the user speaking, but doesn't engage and take the initiative.
        // This overrides the default behavior: "Sorry I didn't understand that"
        furhat.attend(it.userId) // attend the user that spoke
        furhat.gesture(hearSpeechGesture)
        reentry()
    }
    /** Handle Attention switching, see also default attention behaviour in parent state **/
    onUserAttend {
        furhat.attend(it)
        reentry()
    }
}
val ConfirmUnderstanding: State = state {
    onEntry {
        furhat.listen(5000) // Listen for user confirmation
    }

    onResponse<ConfirmIntent> {
        val keywords = listOf("computer science", "cybersecurity", "CS major", "four-year plan")
        // TODO -> Make it to where it we dont stacticlly add irrelevantWords and to repeat what was said
        val irrelevantWords = listOf("eat", "destroy", "hate", "kill", "lick", "marry", "throw", "crush")

        val validTopic = keywords.find { topic ->
            lastHeard?.lowercase()?.matches(Regex(".*\\b$topic\\b.*")) == true
        }

        val hasIrrelevantWord = irrelevantWords.any { badWord ->
            lastHeard?.lowercase()?.contains(badWord) == true
        }

        if (validTopic != null && !hasIrrelevantWord) {
            discussedTopics.add(validTopic)
            when (validTopic) {
                "computer science" -> furhat.say(phrases.A_ComputerScience)
                "cybersecurity" -> furhat.say(phrases.A_CyberMajor)
                "CS major" -> furhat.say(phrases.A_CSMajor)
                "four-year plan" -> furhat.say(phrases.A_FourYearPlanFallY1)
            }
        } else if (hasIrrelevantWord) {
            furhat.say("That doesn't sound like a real question. Could you rephrase it?")
            furhat.listen()
        } else {
            furhat.say("I'm not sure what you mean. Are you asking about a topic like computer science or cybersecurity?")
            furhat.listen()
        }

        goto(Active)
    }

    onResponse {
        furhat.say("Okay, let's talk about something else.")
        goto(Active)
    }

    onNoResponse {
        furhat.say("I didn't hear a confirmation. Let's try again.")
        goto(Active)
    }
}
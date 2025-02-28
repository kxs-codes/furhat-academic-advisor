package furhatos.app.templateadvancedskill.nlu

import furhatos.nlu.Intent
import furhatos.util.Language

/**
 * Define intents to match a user utterance and assign meaning to what they said.
 * Note that there are more intents available in the Asset Collection in furhat.libraries.standard.NluLib
 **/

class NiceToMeetYouIntent : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "glad to meet you",
            "a pleasure to meet you",
            "nice to see you",
            "great to meet you",
            "happy to see you",
            "very nice to finally meet you",
            "fun to meet up with you"
        )
    }
}

class HowAreYouIntent : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "how are you",
            "how are you doing today",
            "what's up",
            "how are things with you",
            "how's it going?",
            "how are you feeling",
            "how's life",
            "what's going on with you"
        )
    }
}

class HelpIntent : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "I need help",
            "help me please",
            "can someone help me",
            "I need assistance"
        )
    }
}

class WhatIsThisIntent : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "What is this",
            "what am I supposed to say",
            "what should I say",
            "I don't know what to do",
            "what am I supposed to do now",
            "should I say something",
            "what's going on",
            "what is happening here",
            "can someone tell me what is going on"
        )
    }
}
class ComputerScienceIntent : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "what is computer science",
            "tell me about computer science",
            "can you explain computer science",
            "define computer science"
        )
    }
}
class FourYearPlanFY1Intent : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "four year plan",
            "what should I take my first semester",
            "what classes should i take for my first semester",
            "Fall freshman year plan"
        )
    }
}
class DepartmentIntent : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "Departments",
            "Department",
            "tell me about department of computer science"
        )
    }
}
class CsMajorIntent : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "computing major",
            "Tell me about the computer science major"
        )
    }
}
class CyberMajorIntent : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "Cyber security major",
            "Cyber security",
            "Tell me about the Cyber security major"
        )
    }
}
class ConfirmIntent : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "yes",
            "yeah",
            "that's correct",
            "correct",
            "exactly",
            "that's right",
            "yep",
            "sure"
        )
    }
}

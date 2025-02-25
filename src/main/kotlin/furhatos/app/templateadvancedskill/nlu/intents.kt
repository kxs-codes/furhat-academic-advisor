package furhatos.app.templateadvancedskill.nlu

import furhatos.nlu.Intent
import furhatos.nlu.ListEntity
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
class LastNameQueryIntent(
    var firstName: FirstName? = null
) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "What's @firstName's last name?",
            "What is the last name of @firstName",
            "Tell me @firstName's surname"
        )
    }
}
class CreditHoursIntent(
    var cpscCourse: CpscCourse? = null
) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "How many credit hours is @cpscCourse?",
            "What are the credit hours for @cpscCourse?",
            "Tell me the credit hours for @cpscCourse"
        )
    }
}

class CourseNameIntent(
    var cpscCourse: CpscCourse? = null
) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "What is @cpscCourse?",
            "What is the name of @cpscCourse?"
        )
    }
}

class FirstName : furhatos.nlu.EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Mark", "Jacob", "Larry", "Harry", "John", "Tim")
    }
}

class FourYearPlanIntent(
    var year: ClassYear? = null,
    var semester: Semester? = null
) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "What classes should I take my @year @semester semester?",
            "What should I take @year in my @year @semester semester?",
            "Tell me my @year @semester plan",
        )
    }
}
class ClassYear : furhatos.nlu.EnumEntity(){
    override fun getEnum(lang: Language): List<String> {
        return listOf(
            "Freshman", "Sophomore", "Junior","Senior",
            "First Year", "Second Year", "Third Year", "Fourth Year"
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

class ClassCoursesIntent(
    var cpscCourse: CpscCourse? = null,
    var semester: Semester? = null
) : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "When is @cpscCourse offered?",
            "is @cpscCourse offered in the @semester?",
            "Can I take @cpscCourse in the @semester?",
            "What semester is @cpscCourse available?",
            "@cpscCourse is offered in which semester?"
        )
    }
}
class CpscCourseList : ListEntity<CpscCourse>()
class CpscCourse : furhatos.nlu.EnumEntity(){
    override fun getEnum(lang: Language): List<String> {
        return listOf(
            "cpsc 130", "MATH 120", "MATH 125", "CPSC 146",
            "STAT 152", "CPSC 207", "CPSC 246", "CPSC 370", "CPSC 323",
            "CPSC 327", "CPSC 376", "CPSC 300", "CPSC 374", "CPSC 311",
            "CPSC 423", "CPSC 476", "CPSC 488", "CPSC 474"
        )
    }
}
class Semester: furhatos.nlu.EnumEntity(){
    override fun getEnum(lang: Language): List<String> {
        return listOf("Spring", "Fall", "Both")
    }
}
class GpaIntent : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "what gpa do i need for the computer science major?",
            "what is the minimum gpa for the computer science department?"

        )
    }
}
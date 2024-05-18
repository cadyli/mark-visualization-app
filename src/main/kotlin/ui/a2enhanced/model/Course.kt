package ui.a2enhanced.model

enum class Term {
    F20, W21, S21, F21, W22, S22, F22, W23, S23, F23,
}

class Course(var code: String, var term: Term, var grade: String ) {
}
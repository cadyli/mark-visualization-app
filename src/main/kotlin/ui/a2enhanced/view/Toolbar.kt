package ui.a2enhanced.view

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import ui.a2enhanced.model.Course
import ui.a2enhanced.model.CourseModel
import ui.a2enhanced.model.Term

class Toolbar(courseModel: CourseModel): HBox() {
    // View to add new courses (bottom half of toolbar)

    // initialise input field widgets
    // for course code input
    val emptyCourseCodeTextField: TextField = TextField().apply{
        alignment = Pos.CENTER
        background = Background(BackgroundFill(Color.WHITE,null,null))
        minWidth = 80.0; prefWidth = 80.0; maxWidth = 80.0
        minHeight = 25.0; prefHeight = 25.0; maxHeight = 25.0
    }

    // for course term input
    val emptyCourseTermChoiceBox = ChoiceBox<Term>().apply {
        minWidth = 60.0; prefWidth = 60.0; maxWidth = 60.0
        minHeight = 25.0; prefHeight = 25.0; maxHeight = 25.0
        items.addAll(*Term.values())
    }

    // for course grade input
    val emptyCourseGradeTextField = TextField().apply {
        minWidth = 40.0; prefWidth = 40.0; maxWidth = 40.0
        minHeight = 25.0; prefHeight = 25.0; maxHeight = 25.0
    }

    // button to create a new course
    val createBtn = Button("Create").apply {
        minWidth = 60.0; prefWidth = 60.0; maxWidth = 60.0
        minHeight = 25.0; prefHeight = 25.0; maxHeight = 25.0
    }



    init{
        children.addAll(emptyCourseCodeTextField,emptyCourseTermChoiceBox,emptyCourseGradeTextField,createBtn)
        setMargin(createBtn, Insets(0.0, 0.0, 0.0, 75.0))
        // configure HBox
        background = Background(BackgroundFill(Color.LIGHTGRAY,null,null))
        padding = Insets(10.0)
        spacing = 15.0
        // combine view and controller - view functions as ViewController to request modification of the model
        createBtn.setOnAction{
            courseModel.addCourse(Course(emptyCourseCodeTextField.text,emptyCourseTermChoiceBox.value,emptyCourseGradeTextField.text))
        }
    }

}
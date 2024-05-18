package ui.a2enhanced.view


import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment
import ui.a2enhanced.model.Course
import ui.a2enhanced.model.CourseModel
import ui.a2enhanced.model.Term
class CourseListVBox(private val courseModel: CourseModel): VBox(), View {
    var courseWidgetsHBoxes = mutableListOf<HBox>()
    init {
        courseModel.addView(this) // subscribe to the Model
        update() // call to update the view of all the courses
        // configure view of VBox
        spacing = 10.0
        padding = Insets(10.0,5.0,10.0,5.0)

    }
    override fun update() {
        // reset mutable list of HBoxes: remove all existing HBoxes in the mutable list
        courseWidgetsHBoxes.clear()
        // render a HBox for each course
        courseModel.getCourses().map { course ->
            val updateBtn = Button("Update")
            val deleteBtn = Button("Delete")
            val courseCodeLabel = Label(course.code).apply{
                textAlignment = TextAlignment.CENTER
                alignment = Pos.CENTER
                background = Background(BackgroundFill(Color.WHITE,null,null))
                minWidth = 80.0; prefWidth = 80.0; maxWidth = 80.0
                minHeight = 25.0; prefHeight = 25.0; maxHeight = 25.0
            }

            val courseTermChoiceBox = ChoiceBox<Term>().apply {
                minWidth = 60.0; prefWidth = 60.0; maxWidth = 60.0
                minHeight = 25.0; prefHeight = 25.0; maxHeight = 25.0
                items.addAll(*Term.values())
                value = course.term
                //  combine view and controller (ViewController) - view also functions as controller to enable update button and change delete button to undo button on user input
                valueProperty().addListener { _, _, _ ->
                    updateBtn.isDisable = false
                    deleteBtn.text = "Undo"
                }
            }
            var courseGradeTextField = TextField(course.grade).apply{
                minWidth = 40.0; prefWidth = 40.0; maxWidth = 40.0
                minHeight = 25.0; prefHeight = 25.0; maxHeight = 25.0
                //  combine view and controller (ViewController) - view also functions as controller to enable update button and change delete button to undo button on user input
                textProperty().addListener{_,_,newValue ->
                    if (newValue == "WD" || (newValue.toIntOrNull() != null && newValue.toInt() in 0..100)) {
                        updateBtn.isDisable = false
                        deleteBtn.text = "Undo"
                    }
                }
            }
            updateBtn.apply {
                minWidth = 60.0; prefWidth = 60.0; maxWidth = 60.0
                minHeight = 25.0; prefHeight = 25.0; maxHeight = 25.0
                isDisable = true
                //  combine view and controller (ViewController) - view also functions as controller to request modification of model
                onAction = EventHandler {
                    courseModel.updateCourse(Course(course.code,courseTermChoiceBox.value,courseGradeTextField.text))
                }
            }
            deleteBtn.apply {
                minWidth = 60.0; prefWidth = 60.0; maxWidth = 60.0
                minHeight = 25.0; prefHeight = 25.0; maxHeight = 25.0
                //  combine view and controller (ViewController) - view also functions as controller to request modification of model
                onAction = EventHandler {
                    if (text == "Delete"){
                        //   function as delete button
                        courseModel.removeCourse(course)
                    }
                    else{
                        //  function as undo button
                        courseModel.resetCourseList()
                    }
                }
            }
            val tempHBox = HBox().apply{
                children.addAll(courseCodeLabel,courseTermChoiceBox,courseGradeTextField,updateBtn,deleteBtn)
                // configure background colour of the course list items
                background = when (course.grade.toIntOrNull()){
                    null -> Background(BackgroundFill(Color.DARKSLATEGRAY, null,null))
                    in 0..49 -> Background(BackgroundFill(Color.LIGHTCORAL,null,null))
                    in 50 .. 59 -> Background(BackgroundFill(Color.LIGHTBLUE,null,null))
                    in 60 .. 90 -> Background(BackgroundFill(Color.LIGHTGREEN,null,null))
                    in 91 .. 96 -> Background(BackgroundFill(Color.SILVER,null,null))
                    else -> Background(BackgroundFill(Color.GOLD,null,null))
                }
                // style HBox
                padding = Insets(10.0)
                alignment = Pos.CENTER
                spacing = 15.0
            }
            // add each HBox into the mutable list of HBoxes
            courseWidgetsHBoxes.add(tempHBox)
        }

        children.setAll(courseWidgetsHBoxes)

    }
}
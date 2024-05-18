package ui.a2enhanced

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.TabPane.TabClosingPolicy
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import ui.a2enhanced.model.CourseModel
import ui.a2enhanced.view.*

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val model = CourseModel()
        // tool bar: HBox
        val toolBarHBox = Toolbar(model)
        // course list: scroll pane
        val courseListScrollPane = ScrollPane().apply{
            content = CourseListVBox(model)
            vbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS
            isFitToWidth = true
            style = "-fx-background-color: transparent;"
        }
        // use VBox layout for admin part
        val courseAdminSectionVBox = VBox(toolBarHBox, courseListScrollPane)
        VBox.setMargin(toolBarHBox, Insets(10.0,20.0,0.0,5.0))

        // bottom status bar
        val bottomStatusBarHBox = BottomStatusBarHBox(model)

        // visualisation section on the right
        val visualisationTabOne = VisualisationTabOne(model)
        val visualisationTabTwo = VisualisationTabTwo(model)
        val visualisationTabThree = VisualisationTabThree(model)
        val visualisationTabFour = VisualisationTabFour()
        val visualisationSectionTabPane = TabPane().apply {
            tabs.add(Tab("Average by Term", visualisationTabOne))
            tabs.add(Tab("Progress Towards Degree", visualisationTabTwo))
            tabs.add(Tab("Course Outcomes",visualisationTabThree ))
            tabs.add(Tab("Incremental Average",visualisationTabFour ))
        }
        visualisationSectionTabPane.tabClosingPolicy = TabClosingPolicy.UNAVAILABLE

        // use border pane to combine all elements
        val root = BorderPane(visualisationSectionTabPane,null,null,bottomStatusBarHBox, courseAdminSectionVBox)
        // stage
        stage.apply {
            title = "CS349 - A2 My Mark Visualisation - c66li"
            scene = Scene(root, 900.0, 450.0)
        }
        stage.show()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}
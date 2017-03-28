package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.I18nSupport

import scala.collection.mutable.ArrayBuffer
import models._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
class HomeController @Inject() (components: ControllerComponents) extends AbstractController(components) with I18nSupport {

  private val widgets = ArrayBuffer(
    Widget("S", "Widget 1", 123),
    Widget("N", "Widget 2", 456),
    Widget("S", "Widget 3", 789)
  )

  def listWidgets = Action { implicit request =>
    // Pass an unpopulated form to the template
    Ok(views.html.listWidgets(widgets.toSeq, HomeController.createWidgetForm))
  }

  // This will be the action that handles our form post
  def createWidget = Action { implicit request =>
    val formValidationResult = HomeController.createWidgetForm.bindFromRequest
    formValidationResult.fold({ formWithErrors =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.listWidgets(widgets.toSeq, formWithErrors))
    }, { widget =>
      // This is the good case, where the form was successfully parsed as a Widget.
      widgets.append(widget)
      Redirect(routes.HomeController.listWidgets)
    })
  }

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action { implicit request =>
    Ok(views.html.index())
  }
}

object HomeController {

  /** The form definition for the "create a widget" form.
    *  It specifies the form fields and their types,
    *  as well as how to convert from a Widget to form data and vice versa.
    */
  val createWidgetForm = Form(
    mapping(
      "type1" -> text,
      "name" -> text,
      "price" -> number
    )(Widget.apply)(Widget.unapply)
  )

}

package controllers

import javax.inject._

import cats.data.{Validated, ValidatedNel}
import com.faacets.core.{Expr, Scenario}
import com.faacets.data.Textable
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.I18nSupport

import scala.collection.mutable.ArrayBuffer
import models._
import play.api.data.format.Formatter
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}
import scalin.immutable.Vec
import spire.math.Rational

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

  def enterUserExpr = Action { implicit request =>
    // Pass an unpopulated form to the template
    Ok(views.html.userExpr(HomeController.enterUserExprForm))
  }

  def parseUserExpr = Action { implicit request =>
    val formValidationResult = HomeController.enterUserExprForm.bindFromRequest
    formValidationResult.fold({ formWithErrors =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.userExpr(formWithErrors))
    }, { userExpr =>
      // This is the good case, where the form was successfully parsed as a Widget.
      // widgets.append(widget)
      val dExpr = userExpr.toDExpr
      val expr = dExpr.split._1
      expr.symmetryGroup
      import com.faacets.defaults._
      import com.faacets.operation._
      Ok(views.html.userExpr(formValidationResult, expr.asYaml, ProductExtractor[Expr].forceExtract(expr).asYaml))
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

  import UserVecRational.userVecRationalTextable

  val enterUserExprForm = Form(
    mapping(
      "representation" -> of[Representation],
      "scenario" -> of[Scenario].verifying(smallScenario),
      "coefficients" -> text
    )(UserExpr.apply)(UserExpr.unapply).verifying(UserExpr.constraint)
  )

  lazy val smallScenario: Constraint[Scenario] = Constraint[Scenario]("") {
    scenario =>
      if (scenario.nParties < 4 && scenario.maxNumInputs < 5 && scenario.maxNumOutputs < 5 && scenario.shapeP.size <= 100)
        Valid
      else
        Invalid(Seq(ValidationError("In the web interface, we only support small scenarios with nParties < 4, nInputs < 5, nOutputs < 5, and a maximum number of coefficients of 100")))
  }

  implicit lazy val scenarioFormatter: Formatter[Scenario] = formatterFromTextable[Scenario](None)
  implicit lazy val vecRationalFormatter: Formatter[Vec[Rational]] = formatterFromTextable[Vec[Rational]](None)

  def formatterFromTextable[T:Textable](defaultIfEmpty: Option[T]): Formatter[T] = new Formatter[T] {

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], T] =
    data.get(key) match {
      case Some(string) => Textable[T].fromText(string) match {
        case Validated.Invalid(errors) => Left(errors.toList.map(err => FormError(key, err)))
        case Validated.Valid(t) => Right(t)
      }
      case None => defaultIfEmpty match {
        case Some(default) => Right(default)
        case None => Left(Seq(FormError(key, "error.required")))
      }
        Left(Seq(FormError(key, "error.required")))
    }

    def unbind(key: String, value: T): Map[String, String] = Map(key -> Textable[T].toText(value))

  }

}

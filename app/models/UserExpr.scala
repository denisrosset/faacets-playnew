package models

import cats.data.Validated
import com.faacets.defaults._
import com.faacets.core._
import play.api.data.format.Formatter
import play.api.data.{FormError, Mapping}
import fastparse.noApi._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}
import scalin.immutable.{Mat, Vec}
import scalin.immutable.dense._

case class UserExpr(representation: Representation, scenario: Scenario, coefficients: Vec[Rational]) {

  def toDExpr: DExpr = representation.validateExpr(scenario, coefficients).getOrElse(sys.error("Cannot happen"))

}

object UserExpr {

  val constraint: Constraint[UserExpr] = Constraint[UserExpr]("constraints.userexprcheck") {
    case UserExpr(r, s, c) => r.validateExpr(s, c) match {
      case Validated.Invalid(errors) => Invalid(errors.toList.map(ValidationError(_)))
      case Validated.Valid(_) => Valid
    }
  }

}

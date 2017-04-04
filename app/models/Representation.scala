package models

import cats.data.{Validated, ValidatedNel}
import com.faacets.core.{DExpr, Expr, Scenario}
import com.faacets.data.Textable
import play.api.data.FormError
import play.api.data.format.Formatter
import scalin.immutable.Vec
import spire.math.Rational

import UserVecRational.userVecRationalTextable

sealed abstract class Representation(val textValue: String) {

  def validateExpr(scenario: Scenario, coeffString: String): ValidatedNel[String, DExpr]

}

object Representation {

  case object ProbabilityVector extends Representation("PVec") {

    def validateExpr(scenario: Scenario, coeffString: String) =
      userVecRationalTextable.fromText(coeffString) andThen { coeffs => DExpr.validate(scenario, coeffs) }

  }

  case object CollinsGisinVector extends Representation("CGVec") {

    def validateExpr(scenario: Scenario, coeffString: String) =
      Expr.parseCollinsGisinVector(scenario, coeffString).map(_.toDExpr)

  }

  case object CorrelatorsVector extends Representation("CorrVec") {

    def validateExpr(scenario: Scenario, coeffString: String) =
      Expr.parseCorrelatorsVector(scenario, coeffString).map(_.toDExpr)

  }

  case object Expression extends Representation("Expr") {

    def validateExpr(scenario: Scenario, expression: String) = DExpr.parseExpression(scenario, expression)

  }

  val valid = Set(ProbabilityVector, CollinsGisinVector, CorrelatorsVector, Expression).map(rep => (rep.textValue, rep)).toMap

  implicit lazy val formatter: Formatter[Representation] = new Formatter[Representation] {

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Representation] =
      data.get(key) match {
        case Some(string) => valid.get(string) match {
          case Some(rep) => Right(rep)
          case None => Left(Seq(FormError(key, "incorrect representation name")))
        }
        case None => Left(Seq(FormError(key, "error.required")))
      }

    override def unbind(key: String, value: Representation): Map[String, String] = Map(key -> value.textValue)

  }

}

package models

import cats.data.{Validated, ValidatedNel}
import com.faacets.core.{DExpr, Expr, Scenario}
import play.api.data.FormError
import play.api.data.format.Formatter
import scalin.immutable.Vec
import spire.math.Rational

sealed abstract class Representation(val textValue: String) {

  def validateExpr(scenario: Scenario, coefficients: Vec[Rational]): ValidatedNel[String, DExpr]

}

object Representation {

  case object ProbabilityVector extends Representation("PVec") {

    def validateExpr(scenario: Scenario, coefficients: Vec[Rational]) = DExpr.validate(scenario, coefficients)

  }

  case object CollinsGisinVector extends Representation("CGVec") {

    def validateExpr(scenario: Scenario, coefficients: Vec[Rational]) =
      if (coefficients.length != scenario.shapeNG.size)
        Validated.invalidNel(s"Incorrect coefficient vector length ${coefficients.length}, should be ${scenario.shapeNG.size}")
      else
        Validated.valid(Expr.collinsGisin(scenario, coefficients).toDExpr)

  }

  case object CorrelatorsVector extends Representation("CorrVec") {

    def validateExpr(scenario: Scenario, coefficients: Vec[Rational]) =
      if (scenario.minNumOutputs < 2 || scenario.maxNumOutputs > 2)
        Validated.invalidNel(s"Correlators are only defined for scenarios with binary outputs")
      else if (coefficients.length != scenario.shapeNC.size)
        Validated.invalidNel(s"Incorrect coefficient vector length ${coefficients.length} should be ${scenario.shapeNG.size}")
      else
        Validated.valid(Expr.correlators(scenario, coefficients).toDExpr)
  }

  val valid = Set(ProbabilityVector, CollinsGisinVector, CorrelatorsVector).map(rep => (rep.textValue, rep)).toMap

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

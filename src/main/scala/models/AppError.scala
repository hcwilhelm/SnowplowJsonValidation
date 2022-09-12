package models

import scala.util.control.NoStackTrace

sealed trait AppError extends NoStackTrace with Product

object AppError {

  final case class SchemaParsingError(id: String) extends AppError {
    override def toString: String = s"Schema with $id is not valid json"
  }

  final case class SchemaNotFoundError(id: String) extends AppError {
    override def toString: String = s"Schema with $id not found"
  }

  final case class SchemaValidationError(message: String) extends AppError {
    override def toString: String = message
  }

  final case class DBError(error: Exception) extends AppError {
    override def toString: String = error.getMessage
  }
}

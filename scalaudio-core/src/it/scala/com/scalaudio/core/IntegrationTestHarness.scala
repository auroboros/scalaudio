package com.scalaudio.core

import com.scalaudio.core.syntax.ScalaudioSyntaxHelpers
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 5/30/16.
  */
trait IntegrationTestHarness extends FlatSpec with Matchers with ScalaudioSyntaxHelpers

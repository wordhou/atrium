package ch.tutteli.atrium.spec.assertions

import ch.tutteli.atrium.api.cc.en_UK.contains
import ch.tutteli.atrium.api.cc.en_UK.message
import ch.tutteli.atrium.api.cc.en_UK.toThrow
import ch.tutteli.atrium.assertions.DescriptionBigDecimalAssertions
import ch.tutteli.atrium.creating.Assert
import ch.tutteli.atrium.creating.AssertionPlant
import ch.tutteli.atrium.spec.AssertionVerbFactory
import ch.tutteli.atrium.spec.describeFun
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.include
import java.math.BigDecimal

abstract class BigDecimalAssertionsSpec(
    verbs: AssertionVerbFactory,
    isNumericallyEqualToPair: Pair<String, AssertionPlant<BigDecimal>.(BigDecimal) -> AssertionPlant<BigDecimal>>,
    describePrefix: String = "[Atrium] "
) : Spek({


    include(object : ch.tutteli.atrium.spec.assertions.SubjectLessAssertionSpec<BigDecimal>("$describePrefix[BigDecimal] ",
        isNumericallyEqualToPair.first to mapToCreateAssertion { isNumericallyEqualToPair.second(this, BigDecimal.TEN) }
    ) {})

    include(object : ch.tutteli.atrium.spec.assertions.CheckingAssertionSpec<BigDecimal>(verbs, "$describePrefix[BigDecimal] ",
        checkingTriple(isNumericallyEqualToPair.first, { isNumericallyEqualToPair.second(this, BigDecimal.TEN) }, BigDecimal("10.000"), BigDecimal("10.00001"))
    ) {})

    fun describeFun(vararg funName: String, body: SpecBody.() -> Unit)
        = describeFun(describePrefix, funName, body = body)

    val expect = verbs::checkException
    val assert: (BigDecimal) -> Assert<BigDecimal> = verbs::checkImmediately
    val (isNumericallyEqualTo, isNumericallyEqualToFun) = isNumericallyEqualToPair

    describeFun(isNumericallyEqualTo) {
        mapOf(
            BigDecimal.TEN to BigDecimal("10.00"),
            BigDecimal.ZERO to BigDecimal("0.0"),
            BigDecimal.ZERO to BigDecimal("0."),
            BigDecimal.ZERO to BigDecimal("0.00"),
            BigDecimal.ZERO to BigDecimal("00.0")
        ).forEach { subject, expected ->
            test("subject $subject and expected $expected does not throw") {
                assert(subject).isNumericallyEqualToFun(expected)
            }
        }

        mapOf(
            BigDecimal.TEN to BigDecimal("10.00000001"),
            BigDecimal.ZERO to BigDecimal("0.0000001")
        ).forEach { subject, expected ->
            test("subject $subject and expected $expected throws AssertionError") {
                expect {
                    assert(subject).isNumericallyEqualToFun(expected)
                }.toThrow<AssertionError> {
                    message {
                        contains(subject,
                            "${DescriptionBigDecimalAssertions.IS_NUMERICALLY_EQUAL_TO.getDefault()}: $expected"
                        )
                    }
                }

            }
        }
    }
})


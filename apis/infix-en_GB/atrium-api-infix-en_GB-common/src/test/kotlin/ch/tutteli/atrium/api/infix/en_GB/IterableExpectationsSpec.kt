package ch.tutteli.atrium.api.infix.en_GB

import ch.tutteli.atrium.creating.Expect
import ch.tutteli.atrium.specs.*
import kotlin.reflect.KFunction2

class IterableExpectationsSpec : ch.tutteli.atrium.specs.integration.IterableExpectationsSpec(
    getToHaveElementsPair(),
    getNotToHaveElementstPair(),
    minFeaturePair(),
    fun1<Iterable<Int>, Expect<Int>.() -> Unit>(Expect<Iterable<Int>>::min),
    maxFeaturePair(),
    fun1<Iterable<Int>, Expect<Int>.() -> Unit>(Expect<Iterable<Int>>::max),
    getToContainNoDuplicatesPair()
) {
    companion object {
        private val toHave: KFunction2<Expect<Iterable<Int>>, elements, Expect<Iterable<Int>>> = Expect<Iterable<Int>>::toHave
        private fun getToHaveElementsPair() = "${toHave.name} ${next::class.simpleName}" to Companion::toHaveElements
        private fun toHaveElements(expect: Expect<Iterable<Int>>) = expect toHave elements

        private val notToHave: KFunction2<Expect<Iterable<Int>>, elements, Expect<Iterable<Int>>> =
            Expect<Iterable<Int>>::notToHave

        private fun getNotToHaveElementstPair() = "${notToHave.name} ${next::class.simpleName}" to Companion::notToHaveElements
        private fun notToHaveElements(expect: Expect<Iterable<Int>>) = expect notToHave elements

        private fun minFeaturePair() = feature1<Iterable<Int>, o, Int>(Expect<Iterable<Int>>::min).name to ::minFeature
        private fun minFeature(expect: Expect<Iterable<Int>>) = expect min o

        private fun maxFeaturePair() = feature1<Iterable<Int>, o, Int>(Expect<Iterable<Int>>::min).name to ::maxFeature
        private fun maxFeature(expect: Expect<Iterable<Int>>) = expect max o

        private val notToContainDuplicates: KFunction2<Expect<Iterable<Int>>, duplicates, Expect<Iterable<Int>>> =
            Expect<Iterable<Int>>::notToContain

        private fun getToContainNoDuplicatesPair() =
            "${notToContainDuplicates.name} ${duplicates::class.simpleName}" to Companion::toContainNoDuplicates

        private fun toContainNoDuplicates(expect: Expect<Iterable<Int>>) = expect notToContain duplicates

    }

    @Suppress("unused", "UNUSED_VALUE")
    private fun ambiguityTest() {
        var a1: Expect<List<Int>> = notImplemented()
        var a1b: Expect<Set<Double?>> = notImplemented()
        var star: Expect<Collection<*>> = notImplemented()

        a1 = a1 toHave elements
        a1 = a1 notToHave elements
        a1 = a1 notToContain duplicates

        a1b = a1b toHave elements
        a1b = a1b notToHave elements
        a1b = a1b notToContain duplicates

        star = star toHave elements
        star = star notToHave elements
        star = star notToContain duplicates

        //nullable not supported by min/max or rather T : Comparable<T> does not exist for T? (one cannot implement an interface for the nullable type)
        //same for Iterable<*>
        a1 min o toEqual 2
        a1 max o toEqual 3

        a1 = a1 min { }
        a1 = a1 max { }
    }
}

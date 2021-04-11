import org.junit.jupiter.api.Test

class PatternTaskTest {
    val a = PatternTask()

    @Test
    fun test1() {
        assert(a.matches(
            "spamshit228@trololo.xxx",
            "[a-zA-Z0-9]+@[a-zA-Z0-9\\-\\.]+"))
    }

    @Test
    fun test2() {
        assert(a.matches("",""))
    }

    @Test
    fun test3() {
        assert(!a.matches(
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@!$",
            "(aaaaa)+b"))
    }

    @Test
    fun test4() {
        assert(!a.matches(
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@!$",
            "(a+a+a+a+a+a+)+b"
        ))
    }

    @Test
    fun test5() {
        assert(!a.matches("$}])([{^","$}])([{^"))
    }
}
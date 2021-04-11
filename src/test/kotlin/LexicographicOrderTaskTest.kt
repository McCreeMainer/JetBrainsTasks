import org.junit.jupiter.api.Test

class LexicographicOrderTaskTest {

    @Test
    fun test1() {
        assert(
            getAlphabet(listOf("booba", "aboba", "sos", "ses", "sas", "sasageo", "sus")) ==
                    "b, o, e, a, u, s"
        )
    }

    @Test
    fun test2() {
        assert(getAlphabet(listOf("booba", "aboba", "bobs")) == "Impossible")
    }

    @Test
    fun test3() {
        assert(getAlphabet(listOf("a", "b", "c", "cb", "cc", "ca")) == "Impossible")
    }

    @Test
    fun test4() {
        assert(
            getAlphabet(
                listOf(
                    "Дидовец",
                    "Доронин",
                    "Предыбайло",
                    "Устинов",
                    "Лазарев",
                    "Лапин",
                    "Толочко",
                    "Симонов",
                    "Ситников",
                    "Зайцев"
                )
            ) == "Impossible"
        )
    }

    @Test
    fun test5() {
        assert(
            getAlphabet(
                listOf(
                    "Дидовец",
                    "Доронин",
                    "Зайцев",
                    "Предыбайло",
                    "Устинов",
                    "Лазарев",
                    "Лапин",
                    "Толочко",
                    "Симонов",
                    "Ситников",
                )
            ) == "а, и, о, д, з, п, у, л, м, т, с"
        )
    }
}
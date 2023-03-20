import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class UnitTest{
    //@Test
    fun test1(){
        heroes.clear()
        monarchHero = CaoCao()
        heroes.add(monarchHero)
        heroes.add(ZhangFei(MinisterRole()))
        assertTrue(monarchHero.name == "Cao Cao")
    }
    //@Test
    fun test2(){
        if(heroes.size < 2)
            test1()
        assertTrue(heroes.size == 2)
    }
    fun printHerosName(){
        var s = ""
        for (h in heroes){
            s += "${h.name} |"
        }
        println(s)
    }
    @Test
    fun testCaoDodgeAttack() {
        monarchHero = CaoCao()
        heroes.add(monarchHero)
        while (heroes.size < 7){
            heroes.add(NoneMonarchFactory.createRandomHero())
        }
        assertTrue(monarchHero.dodgeAttack())
    }
    @Test
    fun testBeingAttacked(){
        //testCaoDodgeAttack()
        if(heroes.isEmpty()){
            heroes.add(NoneMonarchFactory.createRandomHero())
        }
        for (h in heroes){
            val hero = h
            val spy = object: WarriorHero(MinisterRole()){
                override val name: String = hero.name
                override fun beingAttacked() {
                    hero.beingAttacked()
                    assertTrue(hero.hp >= 0)
                }
            }
            for (i in 0..10){
                spy.beingAttacked()
            }
        }
    }
    @Test
    fun testDiscardCards(){
        val hero = ZhangFei(DummyRole())
        hero.discardCards()
        hero.discardCards()
    }
}

object FakeNonMonarchFactory : GameObjectFactory {
    var i = 0
    override fun getRandomRole(): Role {
        return MinisterRole()
    }

    override fun createRandomHero(): Hero {
        i++
        return when(i){
            1 -> SimaYi(getRandomRole())
            2 -> XuChu(getRandomRole())
            else -> XiaHouyuan(getRandomRole())
        }
    }
    fun hasNext():Boolean{
        return i+1 < 3
    }
}
object FakeMonarchFactory : GameObjectFactory{
    override fun getRandomRole(): Role {
        return MinisterRole()
    }

    override fun createRandomHero(): Hero {
        return CaoCao()
    }
}

class CaoCaoUnit{
    @Test
    fun testCaoDodgeAttack(){
        monarchHero = FakeMonarchFactory.createRandomHero() as MonarchHero
        heroes.add(monarchHero)
        while (FakeNonMonarchFactory.hasNext()){
            heroes.add(FakeNonMonarchFactory.createRandomHero())
        }
        assertTrue(monarchHero.dodgeAttack())
    }
}
class DummyRole(override val roleTitle: String = "") : Role {
    override fun getEnemy(): String {
        return ""
    }
}
package com.softserve.easy.meta;

import com.softserve.easy.meta.metasql.EasyQueryImpl;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class EasyQueryImplTest {

    @Test
    void checkUpdateMatcherLowerCase() {
        EasyQueryImpl easyQuery = new EasyQueryImpl("update ContactEntity set firstName = :nameParam, lastName = :lastNameParam" +
                ", birthDate = :birthDateParam" +
                " where firstName = :nameCode");
        assertThat(easyQuery.matchesUpdate(), equalTo(true));
    }

    @Test
    void checkUpdateMatcherUpperCase() {
        EasyQueryImpl easyQuery = new EasyQueryImpl("UPDATE ContactEntity SET firstName = :nameParam, lastName = :lastNameParam" +
                ", birthDate = :birthDateParam" +
                " WHERE firstName = :nameCode");
        assertThat(easyQuery.matchesUpdate(), equalTo(true));
    }

    @Test
    void checkUpdateMatcherWithoutUpdate() {
        EasyQueryImpl easyQuery = new EasyQueryImpl("ContactEntity set firstName = :nameParam, lastName = :lastNameParam" +
                ", birthDate = :birthDateParam" +
                " where firstName = :nameCode");
        assertThat(easyQuery.matchesUpdate(), equalTo(false));
    }

    @Test
    void checkDeleteMatcherWithoutParameter() {
        EasyQueryImpl easyQuery = new EasyQueryImpl("delete from Emp where id=100");
        assertThat(easyQuery.matchesDelete(), equalTo(false));
    }

    @Test
    void checkDeleteMatcherLowerCase() {
        EasyQueryImpl easyQuery = new EasyQueryImpl("delete from Emp where id= :other");
        assertThat(easyQuery.matchesDelete(), equalTo(true));
    }

    @Test
    void checkDeleteMatcherUpperCase() {
        EasyQueryImpl easyQuery = new EasyQueryImpl("DELETE ContactEntity WHERE firstName = :param");
        assertThat(easyQuery.matchesDelete(), equalTo(true));
    }

    @Test
    void checkDeleteMatcherWithoutDelete() {
        EasyQueryImpl easyQuery = new EasyQueryImpl("ContactEntity WHERE firstName = :param");
        assertThat(easyQuery.matchesDelete(), equalTo(false));
    }

    @Test
    void checkSelectMatcherLowerCase(){
        EasyQueryImpl easyQuery = new EasyQueryImpl("select something from ContactEntity where firstName = :param");
        assertThat(easyQuery.matchesSelect(), equalTo(true));
    }

    @Test
    void checkSelectMatcherUpperCase(){
        EasyQueryImpl easyQuery = new EasyQueryImpl("SELECT something FROM ContactEntity WHERE firstName = :param");
        assertThat(easyQuery.matchesSelect(), equalTo(true));
    }

    @Test
    void checkSelectMatcherWithoutSelect(){
        EasyQueryImpl easyQuery = new EasyQueryImpl("from ContactEntity where firstName = :param");
        assertThat(easyQuery.matchesSelect(), equalTo(false));
    }

    @Test
    void checkSelectMatcherWithoutFrom(){
        EasyQueryImpl easyQuery = new EasyQueryImpl("ContactEntity WHERE firstName = :param");
        assertThat(easyQuery.matchesSelect(), equalTo(false));
    }

    @Test
    void checkDeleteDoesNotMatchSelect(){
        EasyQueryImpl easyQuery = new EasyQueryImpl("delete from Emp where id= :other");
        assertThat(easyQuery.matchesSelect(), equalTo(false));
    }

    @Test
    void checkFromMatcherLowerCase(){
        EasyQueryImpl easyQuery = new EasyQueryImpl("from Employee");
        assertThat(easyQuery.matchesFrom(), equalTo(true));
    }

    @Test
    void checkFromMatcherUpperCase(){
        EasyQueryImpl easyQuery = new EasyQueryImpl("FROM Employee WHERE a=:b");
        assertThat(easyQuery.matchesFrom(), equalTo(true));
    }

    @Test
    void checkFromDoesNotMatchSelect(){
        EasyQueryImpl easyQuery = new EasyQueryImpl("FROM Employee WHERE a=:b");
        assertThat(easyQuery.matchesSelect(), equalTo(false));
    }
//insert tests
//    @Test
//    void checkInsertMatcherLowerCase(){
//        EasyQueryImpl easyQuery = new EasyQueryImpl("INSERT INTO ContactEntity(firstName, lastName, birthDate) SELECT firstName2, lastName2, birthDate2 FROM ContactEntity2");
//        assertThat(easyQuery.matchesInsert(), equalTo(true));
//    }
//
//    @Test
//    void checkInsertMatcherUpperCase(){
//        EasyQueryImpl easyQuery = new EasyQueryImpl("INSERT INTO ContactEntity(firstName, lastName, birthDate) SELECT firstName2, lastName2, birthDate2 FROM ContactEntity2");
//        assertThat(easyQuery.matchesInsert(), equalTo(true));
//    }
//
//    @Test
//    void checkInsertMatcherWithCondition(){
//        EasyQueryImpl easyQuery = new EasyQueryImpl("insert into purged_accounts(id, code, status) select id, code, status from account where status=:statu");
//        assertThat(easyQuery.matchesInsert(), equalTo(true));
//    }
//
//    @Test
//    void checkMatchersNotMix(){
//        EasyQueryImpl easyQuery = new EasyQueryImpl("insert delete into purged_accounts(id, code, status) select id, code, status from account where status=:statu");
//        assertThat(easyQuery.matchesInsert(), equalTo(false));
//    }

    @Test
    void findTableNameInFromTest(){
        EasyQueryImpl easyQuery = new EasyQueryImpl("from ContactEntity where firstName = :param");
        assertThat(easyQuery.extractClassName(),equalTo("ContactEntity"));
    }

    @Test
    void findTableNameInDeleteTest(){
        EasyQueryImpl easyQuery = new EasyQueryImpl("delete from Emp where id= :other");
        assertThat(easyQuery.extractClassName(),equalTo("Emp"));
    }

    @Test
    void findTableNameInUpdateTest(){
        EasyQueryImpl easyQuery = new EasyQueryImpl("update ContactEntity set firstName = :nameParam, lastName = :lastNameParam");
        assertThat(easyQuery.extractClassName(),equalTo("ContactEntity"));
    }






}

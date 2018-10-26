package com.jonydog.refy.daos;

import com.jonydog.refy.configs.TestConfigs;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.util.RefyErrors;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class )
@ContextConfiguration( classes= TestConfigs.class)
public class ReferenceDAOTest {

    @Autowired
    private ReferenceDAO referenceDAO;

    @Test
    public void saveTwoRefs(){

        Reference ref1 = new Reference();
        ref1.setTitle("Paper A");
        ref1.setKeywords("Random Markov Models");
        ref1.setFilePath("C:/asdsa");
        ref1.setAuthorsNames("Joiny Dof");

        Reference ref2 = new Reference();
        ref2.setTitle("Paper A");
        ref2.setKeywords("Random Markov, Decision Trees");
        ref2.setFilePath("C:/asdsa");
        ref2.setAuthorsNames("Digoo Dof");

        Reference[] refs = new Reference[2];
        refs[0] = ref1;
        refs[1] = ref2;

        this.referenceDAO.save( refs, "./", new RefyErrors());

        Reference[] refsBack = this.referenceDAO.getAllReferences("./");
        Assert.assertEquals( refsBack[1].getAuthorsNames(),  "Digoo Dof" );
    }

}


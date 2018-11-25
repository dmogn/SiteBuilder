/* 
 * SiteBuilder is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov
 */
package sitebuilder;


public class MainTest {
    
    /**
     * Disabled test.
     */
    //@Test
    void testHTMLBuild() {        
        sitebuilder.Site site = new sitebuilder.Site();
        site.process("examples/siriusmg.ru/site.config");
    }
}

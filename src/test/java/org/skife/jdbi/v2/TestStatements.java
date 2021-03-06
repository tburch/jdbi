/*
 * Copyright 2004-2007 Brian McCallister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.skife.jdbi.v2;

import org.skife.jdbi.derby.Tools;

/**
 *
 */
public class TestStatements extends DBITestCase
{
    private BasicHandle h;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        h = openHandle();
    }

    @Override
    public void tearDown() throws Exception
    {
        if (h != null) h.close();
        Tools.stop();
    }

    public void testStatement() throws Exception
    {
        int rows = h.createStatement("insert into something (id, name) values (1, 'eric')").execute();
        assertEquals(1, rows);
    }

    public void testSimpleInsert() throws Exception
    {
        int c = h.insert("insert into something (id, name) values (1, 'eric')");
        assertEquals(1, c);
    }

    public void testUpdate() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.createStatement("update something set name = 'ERIC' where id = 1").execute();
        Something eric = h.createQuery("select * from something where id = 1").map(Something.class).list().get(0);
        assertEquals("ERIC", eric.getName());
    }

    public void testSimpleUpdate() throws Exception
    {
        h.insert("insert into something (id, name) values (1, 'eric')");
        h.update("update something set name = 'cire' where id = 1");
        Something eric = h.createQuery("select * from something where id = 1").map(Something.class).list().get(0);
        assertEquals("cire", eric.getName());
    }

}

/* Copyright 2004-2006 Brian McCallister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.skife.jdbi.v2;

import org.skife.jdbi.v2.exceptions.UnableToCloseResourceException;
import org.skife.jdbi.v2.tweak.TransactionHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class BasicHandle implements Handle
{
    private final TransactionHandler transactions;
    private final Connection connection;

    public BasicHandle(TransactionHandler transactions, Connection connection)
    {
        this.transactions = transactions;
        this.connection = connection;
    }

    public Query<Map<String, Object>> createQuery(String sql)
    {
        return new Query<Map<String, Object>>(new DefaultMapper(),
                                              connection,
                                              sql);
    }


    /**
     * Get the JDBC Connection this Handle uses
     *
     * @return the JDBC Connection this Handle uses
     */
    public Connection getConnection()
    {
        return this.connection;
    }

    public void close()
    {
        try
        {
            connection.close();
        }
        catch (SQLException e)
        {
            throw new UnableToCloseResourceException("Unable to close Connection", e);
        }
    }

    /**
     * Start a transaction
     */
    public void begin()
    {
        transactions.begin(this);
    }

    /**
     * Commit a transaction
     */
    public void commit()
    {
        transactions.commit(this);
    }

    /**
     * Rollback a transaction
     */
    public void rollback()
    {
        transactions.rollback(this);
    }

    public SQLStatement createStatement(String sql)
    {
        return new SQLStatement(connection, sql);
    }

    public int insert(String sql, Object... args)
    {
        return update(sql, args);
    }

    public int update(String sql, Object... args)
    {
        SQLStatement stmt = createStatement(sql);
        int position = 0;
        for (Object arg : args)
        {
            stmt.setObject(position++, arg);
        }
        return stmt.execute();
    }
}
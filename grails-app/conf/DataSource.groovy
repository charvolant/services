/*
    Copyright 2015 Australian National Botanic Gardens

    This file is part of NSL services project.

    Licensed under the Apache License, Version 2.0 (the "License"); you may not
    use this file except in compliance with the License. You may obtain a copy
    of the License at http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import java.sql.Connection

dataSource {
    pooled = true
    jmxExport = true
}
hibernate {
    cache.use_second_level_cache = false
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
    flush.mode = 'manual' // OSIV session flush mode outside of transactional context
}

// environment specific settings
// Note these *should* be overridden in the external config file services-config.groovy, esp the DB passwords :-)
environments {
    development {
        dataSource_nsl {
            pooled = true
            driverClassName = "org.postgresql.Driver"
            username = "nsldev"
            password = "nsldev"
//            dialect = "org.hibernate.dialect.PostgreSQLDialect"
            dialect = "au.org.biodiversity.nsl.ExtendedPostgreSQLDialect"
            url = "jdbc:postgresql://localhost:5432/nsl"
            formatSql = true
//            logSql = true
            //noinspection GroovyAssignabilityCheck
            properties {
                defaultTransactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED
                initialSize = 2
                maxActive = 5
                minEvictableIdleTimeMillis = 1800000
                timeBetweenEvictionRunsMillis = 1800000
                numTestsPerEvictionRun = 3
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = true
                validationQuery = "SELECT 1"
            }
        }
    }
    test {
        dataSource_nsl {
            pooled = true
            driverClassName = "org.postgresql.Driver"
            username = "nsldev"
            password = "nsldev"
            dialect = "org.hibernate.dialect.PostgreSQLDialect"
            dbCreate = "update"
            url = "jdbc:postgresql://localhost:5432/nslimport"
            formatSql = false
            logSql = false
            //noinspection GroovyAssignabilityCheck
            properties {
                defaultTransactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED
                initialSize = 2
                maxActive = 5
                minEvictableIdleTimeMillis = 1800000
                timeBetweenEvictionRunsMillis = 1800000
                numTestsPerEvictionRun = 3
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = true
                validationQuery = "SELECT 1"
            }
        }
    }
    production {
        dataSource_nsl {
            pooled = true
            driverClassName = "org.postgresql.Driver"
            username = "nsldev"
            password = "nsldev"
            dialect = "org.hibernate.dialect.PostgreSQLDialect"
            url = "jdbc:postgresql://localhost:5432/nsl"
            formatSql = false
            logSql = false
            //noinspection GroovyAssignabilityCheck
            properties {
                defaultTransactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED
                initialSize = 2
                maxActive = 10
                minEvictableIdleTimeMillis = 1800000
                timeBetweenEvictionRunsMillis = 1800000
                numTestsPerEvictionRun = 3
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = true
                validationQuery = "SELECT 1"
            }
        }
    }
}

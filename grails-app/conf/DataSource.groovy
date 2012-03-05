dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    username = "coursesearch"
    password = "sz7v8YTVGsV2qvaW"
    loggingSql = false
    validationQuery = "SELECT 1"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
            url = "jdbc:h2:dev_db;MVCC=TRUE"
            driverClassName = "org.h2.Driver"
            username = "sa"
            password = ""
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE"
        }
    }
    production {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
            url = "jdbc:mysql://localhost:3306/course_search?useUnicode=true&amp;characterEncoding=utf8"
        }
    }
}

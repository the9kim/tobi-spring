package spring.ch7_sql_separation.r_reusablity_of_bean_config;

import org.springframework.core.io.Resource;

public interface SqlMapConfig {

    Resource getSqlMapResource();
}

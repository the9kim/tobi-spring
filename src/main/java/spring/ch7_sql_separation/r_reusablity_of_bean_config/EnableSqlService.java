package spring.ch7_sql_separation.r_reusablity_of_bean_config;

import org.springframework.context.annotation.Import;

@Import(value = SqlServiceContext4.class)
public @interface EnableSqlService {

}
